package dev.ovidio.gitautomerge.git.model;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.git.funcoes.GitAutoMergeRelease;
import dev.ovidio.gitautomerge.git.integration.GitCommandExecutor;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class RepositorioGit {

    private GitCommandExecutor git;
    private GitAutoMergeRelease gitAutoMerge;

    public RepositorioGit(File repostorioRootPath, String gitName, String gitEmail) throws BaseException {
        System.out.println("Abrindo repositorio git: "+repostorioRootPath.getAbsolutePath());
        git = new GitCommandExecutor(repostorioRootPath,gitName,gitEmail);
        this.gitAutoMerge = new GitAutoMergeRelease(this);
    }

    public void autoMergeBranchs(String branchOrigemName, Integer versaoOrigem, String commitMessage){
        System.out.println("Inicio do merge automatico");
        Branch branchAtual = recuperaBranchAtual();
        Branch branchOrigem = findBranchByName(branchOrigemName);
        List<ReleaseBranch> branches = recuperaBranchReleaseAbertas();
        branches.stream()
                .filter(b -> b.getVersao() >= versaoOrigem)
                .sorted(Comparator.comparingInt(ReleaseBranch::getVersao))
                .forEach(b -> {
                    System.out.println(b);
                    String mensagemCommit = trataMenssagemCommitMergeRelease(branchOrigem, b, commitMessage);
                    mergeBranch(branchOrigem, b, mensagemCommit);
                });
        String mensagemCommit = trataMenssagemCommitMerge(branchOrigem,branchAtual,commitMessage,versaoOrigem);
        mergeBranch(branchOrigem,branchAtual,mensagemCommit);
        pushBranch(branchAtual);
        branches.stream()
                .forEach(this::pushBranch);
    }

    public void mergeBranch(Branch branchOrigem, Branch branchDestino, String commitMessage){
        git.checkout("-B",branchDestino.getBranchName(),branchDestino.getFullBranchName());
//        git.merge(branchOrigem.getFullBranchName(),"-m \""+commitMessage+"\"");
        var response = git.merge(branchOrigem.getFullBranchName(),"--no-edit");
        if(response.getExitStatusCode() != 0){
            throw new BaseException("Ocorreu algum problema no merge para branch: "+branchDestino.getFullBranchName());
        }
    }

    private String trataMenssagemCommitMergeRelease(Branch branchOrigem, ReleaseBranch branchRelease, String commitMessage){
        commitMessage = commitMessage.replace(":branch_origem", branchOrigem.getBranchName());
        commitMessage = commitMessage.replace(":branch_release", branchRelease.getBranchName());
        return commitMessage.replace(":versao_release", Integer.toString(branchRelease.getVersao()));
    }

    private String trataMenssagemCommitMerge(Branch branchOrigem, Branch branchDestino, String commitMessage, Integer versao){
        commitMessage = commitMessage.replace(":branch_origem", branchOrigem.getBranchName());
        commitMessage = commitMessage.replace(":branch_release", branchDestino.getBranchName());
        return commitMessage.replace(":versao_release", versao.toString());
    }

    public List<ReleaseBranch> recuperaBranchReleaseAbertas(){
        var response = git.branch(" --list -a origin/release/*");
        return Arrays.stream(response.getRetorno().split("\n"))
                .map(ReleaseBranch::new)
                .toList();
    }

    public boolean existeBranch(String branchName){
        System.out.printf("Verificando existencia da branch %s\n",branchName);
        return findBranchByName(branchName) != null;
    }

    public Branch findBranchByName(String branchName){
        var response = git.branch("--list -a %s".formatted(branchName));
        if(Objects.equals(response.getRetorno(), "null")){
            return null;
        }
        return new Branch(branchName);
    }

    public Branch recuperaBranchAtual(){
        var response = git.branch("--show-current");
        return new Branch(response.getRetorno());
    }

    public void pushBranch(Branch branch){
        var response = git.push("origin",branch.getBranchName());
        if(response.getExitStatusCode() != 0){
            throw new BaseException("Ocorreu algum problema no push para branch: "+branch.getFullBranchName());
        }
    }

}
