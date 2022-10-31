package dev.ovidio.gitautomerge;

import dev.ovidio.gitautomerge.comandos.ComandoBaseImpl;
import picocli.CommandLine;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class GitAutoMerge {

    public static void main(String[] args) throws IOException {
        System.exit(new CommandLine(new ComandoBaseImpl()).execute(args));
    }

}
