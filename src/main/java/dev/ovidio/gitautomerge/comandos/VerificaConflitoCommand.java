package dev.ovidio.gitautomerge.comandos;

import dev.ovidio.gitautomerge.exception.BaseException;
import picocli.CommandLine;

@CommandLine.Command(
        name = "verificaConflito",
        aliases = {"vf"},
        description = """
                Verifica se não havera nenhum conflito na execução do comando mergeRelease
                """
)
public class VerificaConflitoCommand extends ComandoBase {


    @CommandLine.Option(
            names = {"-o","--versao"},
            description = "Versão de origem, a menor versão que sera usada para o merge automatico",
            required = true)
    private String versaoOrigem;

    @Override
    public Integer executa() throws BaseException {
        System.out.println("verificaConflito");
        return 0;
    }
}
