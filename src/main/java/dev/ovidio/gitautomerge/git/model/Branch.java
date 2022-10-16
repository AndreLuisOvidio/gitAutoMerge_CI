package dev.ovidio.gitautomerge.git.model;

import dev.ovidio.gitautomerge.type.TipoOrigem;
import lombok.Data;

@Data
public class Branch {
    private String branchName;

    private TipoOrigem origemBranch;

    public Branch(String branchName){
        this.origemBranch = TipoOrigem.LOCAL;
        branchName = branchName.trim().replace("remotes/","");
        if(branchName.startsWith("origin/")){
            this.origemBranch = TipoOrigem.REMOTA;
        }
        this.branchName = branchName.replace("origin/","");
    }

    public String getFullBranchName() {
        if(TipoOrigem.REMOTA.equals(origemBranch)){
            return "origin/"+branchName;
        }
        return branchName;
    }

}
