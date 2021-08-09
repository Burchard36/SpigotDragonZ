package com.myplugin.lib.data.json;

public enum PlayerProperty {

    STRENGTH("strength"),
    DEFENSE("defense"),
    MAX_KI("maxKi"),
    MAX_STAMINA("maxStam"),
    MAX_HEALTH("maxHp"),
    KI_POWER("kiPow"),

    TALENT_POINTS("talentPoints"),
    TALENT_POINTS_SPENT("talentPointsSpent"),

    RACE("race"),
    CURRENT_HEALTH("currentHealth"),
    CURRENT_KI("currentKi"),
    CURRENT_STAMINA("currentStam"),
    CURRENT_EXP("currentExp"),
    LEVEL("level");

    private final String name;
    PlayerProperty(final String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
