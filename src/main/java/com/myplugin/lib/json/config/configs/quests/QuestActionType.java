package com.myplugin.lib.json.config.configs.quests;

public enum QuestActionType {
    KILL("KILL"),
    KILL_CUSTOM_MOB("KILL_CUSTOM_MOB"),
    BREAK_BLOCK("BREAK_BLOCK"),
    GATHER_ITEM("GATHER_ITEM");

    private final String path;

    QuestActionType(final String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        return this.path;
    }
}
