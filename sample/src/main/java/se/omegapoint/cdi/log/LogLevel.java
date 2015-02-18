package se.omegapoint.cdi.log;

import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 16/09/14
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public enum LogLevel {

    All(Level.ALL),
    Finest(Level.FINEST),
    Finer(Level.FINER),
    Fine(Level.FINE),
    Config(Level.CONFIG),
    Info(Level.INFO),
    Warning(Level.WARNING),
    Severe(Level.SEVERE),
    ;
    public final Level level;
    LogLevel(Level level){
        this.level=level;
    }

}
