package flythinker.rlh.log4j.appender;

import org.apache.log4j.Logger;

/**
 * Created by wlhua on 2018/10/26.
 */
public class Log4jDevRun {

    static Logger logger = Logger.getLogger(Log4jDevRun.class.getName());
    static void test1() throws Exception
    {
        logger.info("aaa");
    }
    public static void main(String[] args){
        try {
            test1();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
