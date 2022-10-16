package dev.ovidio.gitautomerge.comandos;

import dev.ovidio.gitautomerge.exception.BaseException;
import dev.ovidio.gitautomerge.git.model.RepositorioGit;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;


@Command(name = "mergeRelease",
        description = """
                Realiza o merge automatico em todas as versões maiores do que a versão de origem
                """,
        aliases = {"ma"}
)
public class MergeReleaseCommand extends ComandoBase {

    @Option(names = {"--git-dir","-g"}, descriptionKey = "GITDIR", required = true)
    File gitDir;

    @Option(
            names = {"--branch-origem"},
            description = "Branch da versão de origem, a menor versão que sera usada para o merge automatico",
            required = true)
    private String branchOrigem;

    @Option(
            names = {"--versao-origem"},
            description = "Branch da versão de origem, a menor versão que sera usada para o merge automatico",
            required = true)
    private Integer versaoOrigem;

    @Option(
            names={"-m","--message"},
            description = """
                    Menssagem do commit de merge parametros permitidos:
                    :branch_origem
                    :branch_release
                    :versao_release
                    
                    Default="Merge branch ':branch_origem' into ':branch_release'"
                    """)
    private String commitMessage = "Merge branch ':branch_origem' into ':branch_release'";

    @Option(
            names={"--git-name"},
            description = "Nome utilizado no commit do git default = 'AutoMerge'")
    private String gitName = "AutoMerge";

    @Option(
            names={"--git-email"},
            description = "Email utilizado no commit do git default = 'automerge@automerge.com'")
    private String gitEmail = "automerge@automerge.com";

    public MergeReleaseCommand() {
    }

    @Override
    public Integer executa() throws BaseException {
        RepositorioGit repositorioGit = new RepositorioGit(gitDir,gitName,gitEmail);
        if(!repositorioGit.existeBranch(branchOrigem)){
            System.err.println("Branch origem não existe");
            return 1;
        }
        repositorioGit.autoMergeBranchs(branchOrigem, versaoOrigem, commitMessage);
        return 0;
    }

}
