package dev.ovidio.gitautomerge.internacionalizacao;

import dev.ovidio.gitautomerge.GitAutoMerge;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

public class Message extends ResourceBundle {

    private boolean isDebug = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
    Map<String,String> mapMenssagens;

    @Override
    protected String handleGetObject(String key) {
        String message = mapMenssagens.get(key);
        return message;
    }

    @Override
    public Enumeration<String> getKeys() {
        if(mapMenssagens == null){
            defineMenssagens(Locale.getDefault());
        }
        return new Vector<String>(mapMenssagens.keySet()).elements();
    }

    public void defineMenssagens(Locale locale){
        List<String> possiveisMessages = criaListaMessageConfig("message",locale);
        for(String messageName : possiveisMessages){
            try (InputStream input = GitAutoMerge.class.getClassLoader().getResourceAsStream(messageName.concat(".properties"))) {
                if (input == null) {
                    continue;
                }

                Properties prop = new Properties();
                prop.load(input);
                mapMenssagens = (Map) prop;

                break;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(mapMenssagens == null) mapMenssagens = new HashMap<>();
    }

    private List<String> criaListaMessageConfig(String propName,Locale locale){
        LinkedList<String> resultList = new LinkedList<>();
        resultList.add(propName);
        if(locale == null){
            return resultList;
        }
        if(locale.getLanguage() != null){
            resultList.addFirst(propName
                    .concat("_")
                    .concat(locale.getLanguage()));
        }
        if(locale.getLanguage() != null && locale.getCountry() != null){
            resultList.addFirst(propName
                    .concat("_")
                    .concat(locale.getLanguage())
                    .concat("_")
                    .concat(locale.getCountry()));
        }
        return resultList;
    }

}
