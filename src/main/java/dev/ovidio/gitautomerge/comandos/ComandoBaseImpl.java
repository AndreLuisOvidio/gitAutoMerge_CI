package dev.ovidio.gitautomerge.comandos;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

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

    @Spec
    CommandSpec spec;

    @Override
    public Integer executa() {
        throw new ParameterException(spec.commandLine(), "Missing required subcommand");
    }
}
