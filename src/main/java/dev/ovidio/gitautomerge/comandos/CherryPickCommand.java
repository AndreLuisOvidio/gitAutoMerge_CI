package dev.ovidio.gitautomerge.comandos;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.git.model.RepositorioGit;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "cherryPick",
        description = """
                Realiza o cherry-pick em todas as versões maiores do que a versão de origem
                """,
        aliases = {"cp"}
)
public class CherryPickCommand extends ComandoBase{

    @CommandLine.Option(names = {"--git-dir","-g"}, descriptionKey = "GITDIR", required = true)
    public File gitDir;

    @CommandLine.Option(
            names={"--git-name"},
            description = "Nome utilizado no commit do git default = 'AutoMerge'")
    public String gitName = "AutoMerge";

    @CommandLine.Option(
            names={"--git-email"},
            description = "Email utilizado no commit do git default = 'automerge@automerge.com'")
    public String gitEmail = "automerge@automerge.com";

    @CommandLine.Option(
            names = {"--versao-origem"},
            description = "Branch da versão de origem, a menor versão que sera usada para o merge automatico",
            required = true)
    public Integer versaoOrigem;

    @CommandLine.Option(
            names={"-c","--commit"},
            description = """
                    Id do commit que sera feito o cherryPick
                    """,
            required = true)
    private String commitId;

    @Override
    public Integer executa() throws BaseException {
        RepositorioGit repositorioGit = new RepositorioGit(gitDir,gitName,gitEmail);
        repositorioGit.autoCherryPick(commitId, versaoOrigem);
        return 0;
    }
}
