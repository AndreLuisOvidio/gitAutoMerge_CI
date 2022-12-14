package dev.ovidio.gitautomerge.git.model;

import dev.ovidio.gitautomerge.exception.BaseException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReleaseBranch extends Branch{

    private int versao;


    public ReleaseBranch(String branchName) {
        super(branchName);
        if(!getFullBranchName().contains("origin/release/")){
            throw new BaseException("A branch não é uma release: "+getBranchName());
        }
        defineVersaoBranch();
    }

    /** Uma release é composta por 3 partes separadas por / e quero a ultima parte que é a versão
     * exmplo de release = origin/release/01000000 */
    private void defineVersaoBranch(){
        String versaoStr = getFullBranchName().split("/")[2];
        if(versaoStr.length() != 8){
            throw new BaseException(String.format("""
                    Formatação da versão errada:
                    Esperado: 00000000 (00_00_00_00)
                    Recebido: %s
                    """,versaoStr));
        }
        this.versao = Integer.parseInt(versaoStr);
    }

}
