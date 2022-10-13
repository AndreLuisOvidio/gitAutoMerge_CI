package dev.ovidio.gitautomerge.git.integration;

import dev.ovidio.gitautomerge.exception.ArgumentoInvalidoException;
import dev.ovidio.gitautomerge.exception.BaseException;
import picocli.CommandLine;
import picocli.CommandLine.Help.Ansi;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

public class GitCommandExecutor {

    private final File diretorioGitRepo;

    public GitCommandExecutor(File diretorioGitRepo) throws BaseException {
        this.diretorioGitRepo = diretorioGitRepo;
        if(!isGitRepository()){
            throw new BaseException("Diretorio não é um repositorio git: "+diretorioGitRepo.getAbsolutePath());
        }
    }

    private GitCommandResponse executaComando(String comando, String ... args) throws BaseException {
        StringBuilder comandoGit = new StringBuilder("git ");
        comandoGit.append(" branch ");
        if(args !=null ){
            Arrays.stream(args)
                    .map(arg -> " "+arg+" ")
                    .forEach(comandoGit::append);
        }
        System.out.println("Executando comando ...: "+comandoGit.toString());
        try {
            var p = Runtime.getRuntime().exec(
                    comandoGit.toString(),
                    new String[]{},
                    diretorioGitRepo);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String linha;
            StringBuilder saidaConsole = new StringBuilder();

            saidaConsole.append(br.readLine());
            while ((linha = br.readLine()) != null) {
                if(linha.isBlank()) continue;
                saidaConsole.append("\n").append(linha);
            }

            p.waitFor();
            GitCommandResponse response = new GitCommandResponse();
            response.setRetorno(saidaConsole.toString());
            response.setExitStatusCode(p.exitValue());

            String printResult;
            if(p.exitValue() == 0){
                printResult = Ansi.AUTO.string("""
                        @|fg(green) Comando executado com sucesso saida:|@
                        --------
                        @|fg(green) %s|@
                        --------
                        """.formatted(saidaConsole));
            }else{
                printResult = Ansi.AUTO.string("""
                        @|fg(red) Comando executado com sucesso saida:|@
                        --------
                        @|fg(red) %s|@
                        --------
                        """.formatted(saidaConsole));
            }
            System.out.println(printResult);
            return response;
        } catch (Exception e) {
            throw new BaseException("Diretorio invalido: "+diretorioGitRepo.getAbsolutePath(),e);
        }
    }

    public GitCommandResponse branch(String ... args) throws BaseException {
        return executaComando("branch",args);
    }

    private boolean isGitRepository() throws BaseException {
        if(!diretorioGitRepo.isDirectory()){
            return false;
        }
        System.out.printf("Verificando se o diretorio: %s é um repositorio git\n",diretorioGitRepo.getAbsolutePath());
        return branch().getExitStatusCode() == 0;
    }



}
