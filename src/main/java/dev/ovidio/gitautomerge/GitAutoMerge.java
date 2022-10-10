package dev.ovidio.gitautomerge;

import dev.ovidio.gitautomerge.comandos.Command;

import java.sql.SQLOutput;

/**
 * Hello world!
 *
 */
public class GitAutoMerge {

    public static void main(String[] args){
        if(args.length == 0){
            System.out.println("Nenhum comando informado, lista de comandos validos:");
            Command.printHelpCommand();
            System.exit(1);
        }
        var comando = Command.getCommand(args[0]);
        comando.exec(args);
    }



}
