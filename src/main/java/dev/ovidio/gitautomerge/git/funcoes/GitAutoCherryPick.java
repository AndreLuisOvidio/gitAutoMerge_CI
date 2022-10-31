package dev.ovidio.gitautomerge.git.funcoes;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.exception.CherryPickException;
import dev.ovidio.gitautomerge.git.model.Branch;
import dev.ovidio.gitautomerge.git.model.ReleaseBranch;
import dev.ovidio.gitautomerge.git.model.RepositorioGit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GitAutoCherryPick extends GitFuncaoBase{

    public GitAutoCherryPick(RepositorioGit repositorioGit) {
        super(repositorioGit);
    }

    public void autoCherryPick(String commitId, Integer versao){
        var branchsRelase = repositorioGit.recuperaBranchReleaseAbertas();
        List<Branch> falhasCherryPick = new ArrayList<>();
        List<Branch> sucessoCherryPick = new ArrayList<>();
        branchsRelase.stream()
                .filter(b -> b.getVersao() >= versao)
                .sorted(Comparator.comparingInt(ReleaseBranch::getVersao))
                .forEach(b -> {
                    System.out.println(b);
                    try{
                        repositorioGit.cherryPick(b, commitId);
                        sucessoCherryPick.add(b);
                        repositorioGit.pushBranch(b);
                    }catch (CherryPickException err){
                        System.err.println(err.getMessage());
                        falhasCherryPick.add(b);
                    }
                });
        System.out.println("Branchs com sucesso: ");
        sucessoCherryPick.forEach(b ->{
            System.out.println(b.getFullBranchName());
        });
        System.err.println("\n\n Branchs com CONFLITO: ");
        falhasCherryPick.forEach(b -> {
            System.out.println(b.getFullBranchName());
        });
        if(!falhasCherryPick.isEmpty()){
            throw new BaseException("Houveram conflitos no cherry-pick automatico");
        }
    }

}
