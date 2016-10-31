/**
 * log
 */
package org.ngame.util;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;

/**
 *
 * @author CYSY
 */
public final class Log extends LogRecord
{

    private static final long serialVersionUID = 1L;

    /**
     * 构造
     *
     * @param level
     * @param msg
     */
    public Log(Level level, String msg)
    {
        super(level, msg);
        this.inferCaller();
    }

    /**
     * 构造
     *
     * @param level
     * @param msg
     * @param param
     */
    public Log(Level level, String msg, Object... param)
    {
        this(level, msg);
        this.setParameters(param);
    }

    /**
     * 计算class和method
     */
    private void inferCaller()
    {
        JavaLangAccess access = SharedSecrets.getJavaLangAccess();
        Throwable throwable = new Throwable();
        int depth = access.getStackTraceDepth(throwable);
        StackTraceElement frame = access.getStackTraceElement(throwable, 2);
        setSourceClassName(frame.getClassName());
        setSourceMethodName(frame.getMethodName());
    }

}
