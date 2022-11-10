package dev.ovidio.gitautomerge.git.model;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.exception.CherryPickException;
import dev.ovidio.gitautomerge.git.funcoes.GitAutoCherryPick;
import dev.ovidio.gitautomerge.git.funcoes.GitVerificaConflitoAutoMerge;
import dev.ovidio.gitautomerge.git.integration.CherryPickResponse;
import dev.ovidio.gitautomerge.git.integration.GitCommandExecutor;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RepositorioGit {

    private GitCommandExecutor git;
    private GitVerificaConflitoAutoMerge gitVerificaConflitoAutoMerge;
    private GitAutoCherryPick gitAutoCherryPick;

    public RepositorioGit(File repostorioRootPath, String gitName, String gitEmail) throws BaseException {
        System.out.println("Abrindo repositorio git: "+repostorioRootPath.getAbsolutePath());
        git = new GitCommandExecutor(repostorioRootPath,gitName,gitEmail);
        this.gitVerificaConflitoAutoMerge = new GitVerificaConflitoAutoMerge(this);
        this.gitAutoCherryPick = new GitAutoCherryPick(this);
    }

    public void autoCherryPick(String commitId, Integer versaoOrigem){
        gitAutoCherryPick.autoCherryPick(commitId,versaoOrigem);
    }

    public CherryPickResponse cherryPick(Branch branch, String commitId){
        git.checkout("-B",branch.getBranchName(),branch.getFullBranchName());
        var response = git.cherryPick("-m 1", commitId);
        if(response.getExitStatusCode() != 0){
            git.cherryPick("--abort");
            throw new CherryPickException("Ocorreu algum problema/conflito no cherry-pick para branch: "+branch.getFullBranchName());
        }
        var commitIdCherryPick = git.revParse("--verify HEAD");
        return new CherryPickResponse(branch.getFullBranchName(), commitIdCherryPick.getRetorno());
    }

    public void verificaConflitoAutoMergeBranchs(String branchOrigemName, Integer versaoOrigem){
        gitVerificaConflitoAutoMerge.autoMergeBranchs(branchOrigemName,versaoOrigem);
    }

    public boolean testeMergeBranch(Branch branchOrigem, Branch branchDestino){
        git.checkout("-B",branchDestino.getBranchName(),branchDestino.getFullBranchName());
//        git.merge(branchOrigem.getFullBranchName(),"-m \""+commitMessage+"\"");
        var response = git.merge(branchOrigem.getFullBranchName(),"--no-edit --no-commit");
        git.merge("--abort");
        return response.getExitStatusCode() == 0;
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
