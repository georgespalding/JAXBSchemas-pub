package se.omegapoint.cdi.config;

import se.omegapoint.cdi.config.Config;
import java.io.InputStream;

/**
 * Knows how to read a particular type of config resource
 */
public interface ConfigResourceReader {
    Config read(InputStream is);
}
