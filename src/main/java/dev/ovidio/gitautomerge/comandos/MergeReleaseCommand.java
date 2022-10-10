package dev.ovidio.gitautomerge.comandos;

import dev.ovidio.gitautomerge.exception.ArgumentoInvalidoException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

public class MergeReleaseCommand extends Command {

    @Override
    public String getDescricao() {
        return """
             gitAutoMerge merge-release "diretorio/repositorio"
             
             Usado para realizar o merge automatico da release menor para a maior exemplo:
             merge para 010100 caso já exista branch 010200 e 020000 e develop
             ira fazer um merge da 010100 -> 010200 -> 020000 -> develop
             """;
    }



    @Override
    protected void execComando(String[] args) {
        var path = (String) Array.get(args,1);
        File repoDir = new File(path);
        if (!repoDir.isDirectory()){
            throw new ArgumentoInvalidoException("Diretorio informado invalido");
        }
        try {
            System.out.println(repoDir.getAbsolutePath());
            var p = Runtime.getRuntime().exec("git branch --list -a origin/release/*",new String[]{},repoDir);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;

            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);

            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            System.out.println(s);
        } catch (Exception e) {
            throw new ArgumentoInvalidoException("Diretorio informado invalido",e);
        }
    }

    @Override
    protected int quantidadeArgs() {
        return 2;
    }
}
