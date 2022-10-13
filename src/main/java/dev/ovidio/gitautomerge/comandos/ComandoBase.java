package dev.ovidio.gitautomerge.comandos;

import dev.ovidio.gitautomerge.exception.BaseException;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        version = "1.0.0",
        mixinStandardHelpOptions = true,
        requiredOptionMarker = '*',
        resourceBundle = "dev.ovidio.gitautomerge.internacionalizacao.Message"
)
public abstract class ComandoBase implements Callable<Integer> {

    public void executaAntes(){ }

    public abstract Integer executa() throws BaseException;

    public void executaApos(){ }

    @Override
    public Integer call() throws Exception {
        try{
            executaAntes();
            return executa();
        }catch (BaseException e){
            System.err.println(e.getMessage());
            return e.getExitCode();
        }finally {
            executaApos();
        }

    }
}
