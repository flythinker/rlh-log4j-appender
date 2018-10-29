package flythinker.rlh.log4j.appender;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by wlhua on 2018/10/29.
 */
public class Log4jRedisAppender extends AppenderSkeleton {

    JedisPool pool;

    private String logName;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(String redisConfig) {
        this.redisConfig = redisConfig;
        pool = JedisPoolUtil.getJedisPoolByRedisConfig(redisConfig);
    }

    private String redisConfig;


    public Log4jRedisAppender() {
    }

    public void activateOptions() {
        //System.out.println("Log4jRedisAppender .... activateOptions ..." +redisConfig +" " + logName);
        pool = JedisPoolUtil.getJedisPoolByRedisConfig(redisConfig);
    }

    public void append(LoggingEvent event) {
        if(this.checkEntryConditions()) {
            this.subAppend(event);
        }
    }

    /**
     * 发送日志到Redis
     * @param log
     */
    private void publishLogToRedis(String log) {
        Jedis client = pool.getResource();
        try {
            client.publish(logName, log);
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
            client = null;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    protected void subAppend(LoggingEvent event) {
        //this.qw.write(this.layout.format(event));
        StringBuilder buf = new StringBuilder();
        buf.append(this.layout.format(event));
        if(this.layout.ignoresThrowable()) {
            String[] s = event.getThrowableStrRep();
            if(s != null) {
                int len = s.length;
                for(int i = 0; i < len; ++i) {
                    buf.append(s[i]);
                    buf.append(Layout.LINE_SEP);
                }
            }
        }
        //System.out.println(buf.toString());
        publishLogToRedis(buf.toString());
    }

    protected boolean checkEntryConditions() {
        if(this.closed) {
            LogLog.warn("Not allowed to write to a closed appender.");
            return false;
//        } else if(this.qw == null) {
//            this.errorHandler.error("No output stream or file set for the appender named [" + this.name + "].");
//            return false;
        } else if(this.layout == null) {
            this.errorHandler.error("No layout set for the appender named [" + this.name + "].");
            return false;
        } else {
            return true;
        }
    }

    public synchronized void close() {
        if(!this.closed) {
            this.closed = true;
            pool.destroy();
        }
    }

    public synchronized void setErrorHandler(ErrorHandler eh) {
        if(eh == null) {
            LogLog.warn("You have tried to set a null error-handler.");
        } else {
            this.errorHandler = eh;
        }
    }


    public boolean requiresLayout() {
        return true;
    }
}
