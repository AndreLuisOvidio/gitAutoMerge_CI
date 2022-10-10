package dev.ovidio.gitautomerge.exception;

public class ArgumentoInvalidoException extends BaseException {

    public ArgumentoInvalidoException(String msg){
        super(msg);
    }

    public ArgumentoInvalidoException(String msg, Exception e){
        super(msg,e);
    }
}
