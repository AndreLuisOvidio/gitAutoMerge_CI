package dev.ovidio.gitautomerge.git.integration;

import lombok.Data;

@Data
public class CherryPickResponse {

    private String branchNome;
    private String commitId;

    public CherryPickResponse(String branchNome, String commitId){
        this.branchNome = branchNome;
        this.commitId = commitId;
    }

    public String toString(){

        return branchNome+" - "+commitId;
    }

}
