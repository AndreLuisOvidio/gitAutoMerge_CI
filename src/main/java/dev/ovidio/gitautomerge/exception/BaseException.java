package dev.ovidio.gitautomerge.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{

    private int exitCode = 1;

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String msg, Exception e) {
        super(msg,e);
    }
}
