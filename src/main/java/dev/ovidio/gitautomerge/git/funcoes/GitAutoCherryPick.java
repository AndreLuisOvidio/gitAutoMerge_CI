package dev.ovidio.gitautomerge.git.funcoes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.exception.CherryPickException;
import dev.ovidio.gitautomerge.git.integration.CherryPickResponse;
import dev.ovidio.gitautomerge.git.model.Branch;
import dev.ovidio.gitautomerge.git.model.ReleaseBranch;
import dev.ovidio.gitautomerge.git.model.RepositorioGit;

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
        gerLogSucesso(sucessoCherryPick);

        geraLogFalha(falhasCherryPick);

        if(!falhasCherryPick.isEmpty()){
            throw new BaseException("Houveram conflitos no cherry-pick automatico");
        }
    }

	private void geraLogFalha(List<Branch> falhasCherryPick) {
		var falhasStr = falhasCherryPick.stream()
                .map(Branch::getFullBranchName)
                .toList()
                .toString();
        System.err.println("Branchs com CONFLITO: "+falhasStr);
	}

	private void gerLogSucesso(List<CherryPickResponse> sucessoCherryPick) {
		var sucessoBuilderStr = new StringBuilder();
        sucessoBuilderStr.append("Branchs com sucesso: \n");
        final var CI_PROJECT_URL = System.getenv("CI_PROJECT_URL");
        for (CherryPickResponse response : sucessoCherryPick){
            sucessoBuilderStr.append(response.toString()+" \n");
            if(CI_PROJECT_URL != null && !CI_PROJECT_URL.isBlank()) {
            	sucessoBuilderStr.append(
            			CI_PROJECT_URL +"/-/commit/"+ response.getCommitId()+" \n"
        			);            
            }
        }
        final var NUMERO_CHAMADO = System.getenv("NUMERO_CHAMADO");
        final var URL_SOFTDESK = System.getenv("URL_SOFTDESK");
        if(NUMERO_CHAMADO != null && !NUMERO_CHAMADO.isBlank()
        		&& URL_SOFTDESK != null && !URL_SOFTDESK.isBlank()) {
        	sucessoBuilderStr.append(URL_SOFTDESK+NUMERO_CHAMADO+"\n");
        }
        System.out.println(sucessoBuilderStr);
	}
    
}
