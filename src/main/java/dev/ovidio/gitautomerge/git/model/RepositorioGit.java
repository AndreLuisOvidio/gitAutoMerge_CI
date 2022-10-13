package dev.ovidio.gitautomerge.git.model;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.git.integration.GitCommandExecutor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class RepositorioGit {

    private GitCommandExecutor git;

    public RepositorioGit(File repostorioRootPath) throws BaseException {
        System.out.println("Abrindo repositorio git: "+repostorioRootPath.getAbsolutePath());
        git = new GitCommandExecutor(repostorioRootPath);
    }

    public void autoMergeBranchs(String branchOrigem){
        List<ReleaseBranch> branches = recuperaBranchReleaseAbertas();
    }

    private List<ReleaseBranch> recuperaBranchReleaseAbertas(){
        var response = git.branch(" --list -a origin/release/*");
        return Arrays.stream(response.getRetorno().split("\n"))
                .map(ReleaseBranch::new)
                .toList();
    }

}
