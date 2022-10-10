package dev.ovidio.gitautomerge.comandos;

import java.util.Map;

public abstract class Command {

    private static Map<String, Command> comandoMap = Map.of(
            "merge-release",new MergeReleaseCommand(),
            "verifica-conflito", new VerificaConflitoCommand()
    );

    public void exec(String args[]){
        if(args.length < quantidadeArgs()){
            System.err.println("Quantidade de argumentos invalida gitAutoMerge help");
            System.exit(1);
        }
        this.execComando(args);
    }

    public static Command getCommand(String comandoStr){
        Command comando = comandoMap.get(comandoStr);
        if(comando != null){
            return comando;
        }
        System.err.printf("Comando \"%s\" invalido, comandos validos",comandoStr);
        printHelpCommand();
        System.exit(1);
        throw new IllegalArgumentException("Comando: \""+comandoStr+"\" não existe");
    }

    public static void printHelpCommand(){
        comandoMap.forEach((key,value) -> System.err.printf("\n    \"%s\": \n%s",key,value.getDescricao()));
    }

    public abstract String getDescricao();

    protected abstract void execComando(String[] args);

    protected abstract int quantidadeArgs();
}
