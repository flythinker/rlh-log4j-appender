package flythinker.rlh.log4j.appender;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;

/**
 * Created by wlhua on 2018/10/25.
 */
public class YamlUtil
{
    public static LinkedHashMap loadAsMap(String filepath){
        Yaml yaml = new Yaml();
        return (LinkedHashMap)yaml.load(filepath);
    }

    public static LinkedHashMap loadAsMap(InputStream ins){
        if(ins == null){
            throw new RuntimeException("ins == null");
        }
        Yaml yaml = new Yaml();
        return (LinkedHashMap)yaml.load(ins);
    }
}
