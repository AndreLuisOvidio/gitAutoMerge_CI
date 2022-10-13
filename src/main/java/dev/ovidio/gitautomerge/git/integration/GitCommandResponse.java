package dev.ovidio.gitautomerge.git.integration;

import lombok.Data;

@Data
public class GitCommandResponse {
    private int exitStatusCode;
    private String retorno;
}
