package dev.ovidio.gitautomerge;

import dev.ovidio.gitautomerge.comandos.ComandoBaseImpl;
import dev.ovidio.gitautomerge.comandos.MergeReleaseCommand;
import picocli.CommandLine;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class GitAutoMerge {

    public static void main(String[] args) throws IOException {
        System.out.print("Everything on the console will cleared");
        System.out.print("\033[H\033[2J\n");
        System.out.flush();
        System.exit(new CommandLine(new ComandoBaseImpl()).execute(args));
    }

}
