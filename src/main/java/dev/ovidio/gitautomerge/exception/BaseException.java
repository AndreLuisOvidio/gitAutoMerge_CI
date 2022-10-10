package dev.ovidio.gitautomerge.exception;

public class BaseException extends RuntimeException{
    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String msg, Exception e) {
        super(msg,e);
    }
}
