package dev.ovidio.gitautomerge.git.integration;

import dev.ovidio.gitautomerge.exception.BaseException;
import picocli.CommandLine.Help.Ansi;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;

public class GitCommandExecutor {

    private final File diretorioGitRepo;

    public GitCommandExecutor(File diretorioGitRepo, String gitName, String gitEmail) throws BaseException {
        this.diretorioGitRepo = diretorioGitRepo;
        if(!isGitRepository()){
            throw new BaseException("Diretorio não é um repositorio git: "+diretorioGitRepo.getAbsolutePath());
        }
        verificaGitConfig();
    }

    private void verificaGitConfig(){
        var configEmail = this.executaComando("config","--get user.email").getRetorno();
        var configName = this.executaComando("config","--get user.name").getRetorno();
        String configErro = "";
        if(configEmail.equals("null")){
            configErro += "git 'user.email' não configurado\n";
        }
        if(configName.equals("null")){
            configErro += "git 'user.name' não configurado\n";
        }
        if(!configErro.isBlank()){
            configErro += "Necessario configurar dentro do repossitorio sem --global\n";
            throw new BaseException(configErro);
        }
    }

    private GitCommandResponse executaComando(String comando, String ... args) throws BaseException {
        StringBuilder comandoGit = new StringBuilder("git ");
        comandoGit.append(" %s ".formatted(comando));
        if(args !=null ){
            Arrays.stream(args)
                    .map(arg -> " "+arg+" ")
                    .forEach(comandoGit::append);
        }
        System.out.println(
                Ansi.AUTO.string(
                "@|fg(blue) Executando comando ...: %s|@")
                        .formatted(comandoGit.toString())
        );
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
            saidaConsole = saidaConsole.toString().equals("null") ? new StringBuilder("") : saidaConsole;
            String printResult;
            if(p.exitValue() == 0){
                printResult = Ansi.AUTO.string("""
                        @|fg(green) Comando executado com sucesso saida:|@
                        @|fg(green) %s|@
                        """.formatted(saidaConsole));
            }else{
                printResult = Ansi.AUTO.string("""
                        @|fg(red) Comando executado com erro saida:|@
                        @|fg(red) %s|@
                        """.formatted(saidaConsole));
            }
            System.out.println(printResult);
            return response;
        } catch (Exception e) {
            throw new BaseException("Diretorio invalido: "+diretorioGitRepo.getAbsolutePath(),e);
        }
    }

    private boolean isGitRepository() throws BaseException {
        if(!diretorioGitRepo.isDirectory()){
            return false;
        }
        System.out.printf("Verificando se o diretorio: %s é um repositorio git\n",diretorioGitRepo.getAbsolutePath());
        return branch().getExitStatusCode() == 0;
    }

    public GitCommandResponse branch(String ... args) throws BaseException {
        return executaComando("branch",args);
    }

    public GitCommandResponse checkout(String ... args){
        return executaComando("checkout",args);
    }

    public GitCommandResponse merge(String ... args){
        return executaComando("merge",args);
    }

    public GitCommandResponse push(String ... args){
        return executaComando("push",args);
    }

}
