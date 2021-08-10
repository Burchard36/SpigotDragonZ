package com.myplugin.lib.json.config.enums;

public enum ConfigPath {

    TALENT_POINTS_PER_LEVEL("GameSettings.TalentPointSettings.TalentPointsPerLevel"),
    MAX_BASE_EXP("GameSettings.BaseMaxExp"),
    EXP_INCREASE_PER_LEVEL("GameSettings.LevelExpIncrease"),

    DEBUG_MODE("DevelopmentSettings.Debug"),

    SAVE_CACHE_INTERVAL("DataSettings.SaveCacheInterval");

    private final String path;

    ConfigPath(final String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        return this.path;
    }
}
