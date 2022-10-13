package dev.ovidio.gitautomerge.comandos;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.git.model.RepositorioGit;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Path;


@Command(name = "mergeRelease",
        description = """
                Realiza o merge automatico em todas as versões maiores do que a versão de origem
                """,
        aliases = {"ma"})
public class MergeReleaseCommand extends ComandoBase {

    @Option(names = {"--git-dir","-g"}, descriptionKey = "GITDIR", required = true)
    File gitDir;

    @Option(
            names = {"-o","--branch-origem"},
            description = "Branch da versão de origem, a menor versão que sera usada para o merge automatico",
            required = true)
    private String branchOrigem;

    @Override
    public Integer executa() throws BaseException {
        RepositorioGit repositorioGit = new RepositorioGit(gitDir);
        repositorioGit.autoMergeBranchs(branchOrigem);
        return 0;
    }
}
