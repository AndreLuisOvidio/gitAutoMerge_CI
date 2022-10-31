package dev.ovidio.gitautomerge.exception;

import dev.ovidio.gitautomerge.git.model.Branch;

public class CherryPickException extends BaseException{

    public CherryPickException(String msg) {
        super(msg);
    }
}
