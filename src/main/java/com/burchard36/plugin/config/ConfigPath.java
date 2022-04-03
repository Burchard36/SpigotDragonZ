package com.burchard36.plugin.config;

public class ConfigPath {

    private final String path;

    private ConfigPath(final String path) {
        this.path = path;
    }

    /**
     * This method is only so String arguments dont get mixed up when working with LocalFile class
     * @param path path to where string i located
     * @return ConfigPath instance
     */
    public static ConfigPath path(final String path) {
        return new ConfigPath(path);
    }

    /**
     * Gets the String value from path variable for use in configs
     * @return read what is above
     */
    public final String get() {
        return this.path;
    }
}
