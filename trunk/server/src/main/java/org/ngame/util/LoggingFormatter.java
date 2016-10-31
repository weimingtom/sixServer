package org.ngame.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * logging格式
 *
 * @author beykery
 */
public final class LoggingFormatter extends Formatter
{

    private final Date dat = new Date();
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 格式化
     *
     * @param record
     * @return
     */
    @Override
    public synchronized final String format(final LogRecord record)
    {
        dat.setTime(record.getMillis());
        String message = record.getMessage();
        Object parameters[] = record.getParameters();
        if (parameters != null && parameters.length > 0)
        {
            if (message.indexOf("{0") >= 0 || message.indexOf("{1") >= 0
                || message.indexOf("{2") >= 0 || message.indexOf("{3") >= 0)
            {
                message = java.text.MessageFormat.format(message, parameters);
            }
        }
        if (message.startsWith("["))
        {
            return "[\"" + df.format(dat) + "\"," + message + "\"," + record.getSourceClassName() + "-->" + record.getSourceMethodName() + "]\n";
        } else
        {
            return "[\"" + df.format(dat) + "\",\"" + message + "\"," + record.getSourceClassName() + "-->" + record.getSourceMethodName() + "]\n";
        }
    }
}
