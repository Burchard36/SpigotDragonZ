package com.myplugin.lib;

public enum ConfigPath {

    TALENT_POINTS_PER_LEVEL("GameSettings.TalentPointSettings.TalentPointsPerLevel"),
    MAX_BASE_EXP("GameSettings.BaseMaxExp"),
    EXP_INCREASE_PER_LEVEL("GameSettings.LevelExpIncrease");

    private final String path;

    ConfigPath(final String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        return this.path;
    }
}
