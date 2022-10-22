package dev.ovidio.gitautomerge.git.funcoes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.git.model.Branch;
import dev.ovidio.gitautomerge.git.model.ReleaseBranch;
import dev.ovidio.gitautomerge.git.model.RepositorioGit;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GitAutoMergeRelease{

    private RepositorioGit repositorioGit;

    public void autoMergeBranchs(String branchOrigemName, Integer versaoOrigem, String commitMessage){
        System.out.println("Inicio do merge automatico");
        Branch branchAtual = repositorioGit.recuperaBranchAtual();
        Branch branchOrigem = repositorioGit.findBranchByName(branchOrigemName);
        List<ReleaseBranch> branches = repositorioGit.recuperaBranchReleaseAbertas();
        branches.stream()
                .filter(b -> b.getVersao() >= versaoOrigem)
                .sorted(Comparator.comparingInt(ReleaseBranch::getVersao))
                .forEach(b -> {
                    System.out.println(b);
                    String mensagemCommit = trataMenssagemCommitMergeRelease(branchOrigem, b, commitMessage);
                    repositorioGit.mergeBranch(branchOrigem, b, mensagemCommit);
                });
        String mensagemCommit = trataMenssagemCommitMerge(branchOrigem,branchAtual,commitMessage,versaoOrigem);
        repositorioGit.mergeBranch(branchOrigem,branchAtual,mensagemCommit);
        repositorioGit.pushBranch(branchAtual);
        branches.stream()
                .forEach(repositorioGit::pushBranch);
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

}