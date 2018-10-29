package flythinker.rlh.log4j.appender;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.QuietWriter;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;

import java.io.*;

/**
 * Created by wlhua on 2018/10/29.
 */
public class DevTestAppender extends AppenderSkeleton {

    protected String target;

    public DevTestAppender() {
        this.target = "System.out";
        this.setTarget(target);
        this.activateOptions();
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String value) {
        this.target = value;
    }

    public void activateOptions() {
    }

    public void append(LoggingEvent event) {
        if(this.checkEntryConditions()) {
            this.subAppend(event);
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
        System.out.println(buf.toString());

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
            //this.writeFooter();
            //this.reset();
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
