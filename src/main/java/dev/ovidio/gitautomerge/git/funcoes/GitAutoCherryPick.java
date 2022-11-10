package dev.ovidio.gitautomerge.git.funcoes;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.exception.CherryPickException;
import dev.ovidio.gitautomerge.git.integration.CherryPickResponse;
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
        List<CherryPickResponse> sucessoCherryPick = new ArrayList<>();
        branchsRelase.stream()
                .filter(b -> b.getVersao() >= versao)
                .sorted(Comparator.comparingInt(ReleaseBranch::getVersao))
                .forEach(b -> {
                    System.out.println(b);
                    try{
                        var cherryPickResponse = repositorioGit.cherryPick(b, commitId);
                        sucessoCherryPick.add(cherryPickResponse);
                        repositorioGit.pushBranch(b);
                    }catch (CherryPickException err){
                        System.err.println(err.getMessage());
                        falhasCherryPick.add(b);
                    }
                });
        var sucessoBuilderStr = new StringBuilder();
        sucessoBuilderStr.append("Branchs com sucesso: \n");
        for (CherryPickResponse response : sucessoCherryPick){
            sucessoBuilderStr.append(response.toString()+" \n");
        }
        System.out.println(sucessoBuilderStr);

        var falhasStr = falhasCherryPick.stream()
                .map(Branch::getFullBranchName)
                .toList()
                .toString();
        System.err.println("Branchs com CONFLITO: "+falhasStr);

        if(!falhasCherryPick.isEmpty()){
            throw new BaseException("Houveram conflitos no cherry-pick automatico");
        }
    }

}
