package rudi.support;

import java.util.HashMap;

/**
 * Central repository for all the code pieces
 */
public class RudiSourceRegistry extends HashMap<String, RudiSource> {

    private static RudiSourceRegistry instance;

    private RudiSource rawSource;

    private RudiSourceRegistry() {
    }

    public static RudiSourceRegistry getInstance() {
        if (null == instance)
            instance = new RudiSourceRegistry();
        return instance;
    }

    public RudiSource getRawSource() {
        return rawSource;
    }

    public void setRawSource(RudiSource rawSource) {
        this.rawSource = rawSource;
    }
}
