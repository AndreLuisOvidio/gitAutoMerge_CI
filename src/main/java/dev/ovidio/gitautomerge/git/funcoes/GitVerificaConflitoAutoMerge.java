package dev.ovidio.gitautomerge.git.funcoes;

import java.util.Comparator;
import java.util.List;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.git.model.Branch;
import dev.ovidio.gitautomerge.git.model.ReleaseBranch;
import dev.ovidio.gitautomerge.git.model.RepositorioGit;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GitVerificaConflitoAutoMerge {

    private RepositorioGit repositorioGit;

    public void autoMergeBranchs(String branchOrigemName, Integer versaoOrigem){
        System.out.println("Inicio do merge automatico");
        Branch branchOrigem = repositorioGit.findBranchByName(branchOrigemName);
        List<ReleaseBranch> branches = repositorioGit.recuperaBranchReleaseAbertas();
        branches = branches.stream()
                .filter(b -> b.getVersao() >= versaoOrigem)
                .sorted(Comparator.comparingInt(ReleaseBranch::getVersao))
                .filter(b -> {
                    System.out.println(b);
                    return !repositorioGit.testeMergeBranch(branchOrigem, b);
                })
                .toList();

        branches.stream()
                .map(Branch::getFullBranchName)
                .forEach(System.out::println);
        if(!branches.isEmpty()){
            throw new BaseException("Existem conflitos para alguma branchs release");
        }
    }
}