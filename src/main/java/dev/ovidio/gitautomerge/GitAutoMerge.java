package dev.ovidio.gitautomerge;

import dev.ovidio.gitautomerge.comandos.ComandoBaseImpl;
import dev.ovidio.gitautomerge.comandos.MergeReleaseCommand;
import picocli.CommandLine;

import java.sql.SQLOutput;

/**
 * Hello world!
 *
 */
public class GitAutoMerge {

    public static void main(String[] args){
        System.exit(new CommandLine(new ComandoBaseImpl()).execute(args));
    }

}
