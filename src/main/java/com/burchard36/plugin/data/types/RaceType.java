package com.burchard36.plugin.data.types;

public enum RaceType {

    NONE("NONE"),
    HUMAN("HUMAN"),
    SAIYAN("SAIYAN");

    private final String nameString;

    RaceType(final String raceName) {
        this.nameString = raceName;
    }

    public final String getNameString() {
        return this.nameString;
    }
}
