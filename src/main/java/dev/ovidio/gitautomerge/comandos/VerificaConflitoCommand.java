package dev.ovidio.gitautomerge.comandos;

public class VerificaConflitoCommand extends Command {
    @Override
    public String getDescricao() {
        return """
            gitAutoMerge verifica-conflito "diretorio/repositorio"
            
            Usado para verificar se não ocorrera nenhum conflito no comando merge-release
            """;
    }

    @Override
    protected void execComando(String[] args) {
        System.out.println("verifica-conflito");
    }

    @Override
    protected int quantidadeArgs() {
        return 2;
    }
}
