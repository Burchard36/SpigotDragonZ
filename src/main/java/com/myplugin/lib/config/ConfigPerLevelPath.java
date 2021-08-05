package com.myplugin.lib.config;

public enum ConfigPerLevelPath {
    SAIYAN_STR_INCREASE("GameSettings.Race.SAIYAN.PerLevel.StrInc"),
    SAIYAN_HP_INCREASE("GameSettings.Race.SAIYAN.PerLevel.MaxHpInc"),
    SAIYAN_MAX_KI_INCREASE("GameSettings.Race.SAIYAN.PerLevel.MaxKiInc"),
    SAIYAN_DEFENSE_INCREASE("GameSettings.Race.SAIYAN.PerLevel.DefInc"),
    SAIYAN_KI_POW_INCREASE("GameSettings.Race.SAIYAN.PerLevel.KiPowInc"),
    SAIYAN_STAM_INCREASE("GameSettings.Race.SAIYAN.PerLevel.StamInc"),

    HALF_SAIYAN_STR_INCREASE("GameSettings.Race.HALF_SAIYAN.PerLevel.StrInc"),
    HALF_SAIYAN_HP_INCREASE("GameSettings.Race.HALF_SAIYAN.PerLevel.MaxHpInc"),
    HALF_SAIYAN_MAX_KI_INCREASE("GameSettings.Race.HALF_SAIYAN.PerLevel.MaxKiInc"),
    HALF_SAIYAN_DEFENSE_INCREASE("GameSettings.Race.HALF_SAIYAN.PerLevel.DefInc"),
    HALF_SAIYAN_KI_POW_INCREASE("GameSettings.Race.HALF_SAIYAN.PerLevel.KiPowInc"),
    HALF_SAIYAN_STAM_INCREASE("GameSettings.Race.HALF_SAIYAN.PerLevel.StamInc");


    private final String path;

    ConfigPerLevelPath(final String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        return this.path;
    }
}
