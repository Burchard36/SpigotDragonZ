package com.myplugin.lib.json.config.enums;

public enum Race {
    SAIYAN("SAIYAN"),
    HALF_SAIYAN("HALF_SAIYAN"),
    NONE("NONE");

    private final String raceName;

    Race(final String raceName) {
        this.raceName = raceName;
    }

    @Override
    public String toString() {
        return this.raceName;
    }
}
