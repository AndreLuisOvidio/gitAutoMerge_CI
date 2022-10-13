package dev.ovidio.gitautomerge.git.model;

import dev.ovidio.gitautomerge.type.TipoOrigem;
import lombok.Data;

@Data
public class Branch {
    private String branchName;

    public Branch(String branchName){
        branchName = branchName.trim().replace("remotes/","");
        this.branchName = branchName;
    }

}
