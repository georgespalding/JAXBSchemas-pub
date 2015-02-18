package se.omegapoint.cdi.config;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 22/09/14
 * Time: 20:36
 * To change this template use File | Settings | File Templates.
 */
public interface Config extends Iterable<Config>{
    String getName();
    String getPath();
    Config getParent();
    String getValue(String key);
    /** alias for iterator() */
    Iterator<Config> children();
}
