package dev.ovidio.gitautomerge.model;

import dev.ovidio.gitautomerge.type.TipoOrigem;
import lombok.Data;

@Data
public class Branch {
    private String branchName;
    private TipoOrigem origem;

}
