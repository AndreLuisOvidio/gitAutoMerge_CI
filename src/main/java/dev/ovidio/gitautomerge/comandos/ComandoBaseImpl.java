package dev.ovidio.gitautomerge.comandos;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "auto-merge",
        description = """
                Programa destinado a automatizar o merge em release de versão
                """,
        subcommands = {
                MergeReleaseCommand.class,
                VerificaConflitoCommand.class
        }
)
public class ComandoBaseImpl extends ComandoBase{

    @Override
    public Integer executa() {
        return 0;
    }
}
