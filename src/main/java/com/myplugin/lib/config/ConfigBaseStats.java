package com.myplugin.lib.config;

public enum ConfigBaseStats {
    SAIYAN_DEFAULT_STRENGTH("GameSettings.TalentPointSettings.Race.SAIYAN.Default.Str"),
    SAIYAN_DEFAULT_MAX_HP("GameSettings.TalentPointSettings.Race.SAIYAN.Default.MaxHp"),
    SAIYAN_DEFAULT_MAX_STAM("GameSettings.TalentPointSettings.Race.SAIYAN.Default.MaxStam"),
    SAIYAN_DEFAULT_MAX_KI("GameSettings.TalentPointSettings.Race.SAIYAN.Default.MaxKi"),
    SAIYAN_DEFAULT_KI_POWER("GameSettings.TalentPointSettings.Race.SAIYAN.Default.KiPow"),
    SAIYAN_DEFAULT_DEFENSE("GameSettings.TalentPointSettings.Race.SAIYAN.Default.Def");


    private final String name;

    ConfigBaseStats(final String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
