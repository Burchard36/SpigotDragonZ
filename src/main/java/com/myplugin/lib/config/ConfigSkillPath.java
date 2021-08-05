package com.myplugin.lib.config;

public enum ConfigSkillPath {

    SAIYAN_STR_INCREASE("GameSettings.Race.SAIYAN.StrInc"),
    SAIYAN_HP_INCREASE("GameSettings.Race.SAIYAN.MaxHpInc"),
    SAIYAN_MAX_KI_INCREASE("GameSettings.Race.SAIYAN.MaxKiInc"),
    SAIYAN_DEFENSE_INCREASE("GameSettings.Race.SAIYAN.DefInc"),
    SAIYAN_KI_POW_INCREASE("GameSettings.Race.SAIYAN.KiPowInc"),
    SAIYAN_STAM_INCREASE("GameSettings.Race.SAIYAN.StamInc"),

    HALF_SAIYAN_STR_INCREASE("GameSettings.Race.HALF_SAIYAN.StrInc"),
    HALF_SAIYAN_HP_INCREASE("GameSettings.Race.HALF_SAIYAN.MaxHpInc"),
    HALF_SAIYAN_MAX_KI_INCREASE("GameSettings.Race.HALF_SAIYAN.MaxKiInc"),
    HALF_SAIYAN_DEFENSE_INCREASE("GameSettings.Race.HALF_SAIYAN.DefInc"),
    HALF_SAIYAN_KI_POW_INCREASE("GameSettings.Race.HALF_SAIYAN.KiPowInc"),
    HALF_SAIYAN_STAM_INCREASE("GameSettings.Race.HALF_SAIYAN.StamInc");

    private final String path;

    ConfigSkillPath(final String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return this.path;
    }
}
