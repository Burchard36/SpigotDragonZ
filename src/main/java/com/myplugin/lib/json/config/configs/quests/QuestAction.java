package com.myplugin.lib.json.config.configs.quests;

public class QuestAction {

    public String type;
    public Object value;
    public int amount;

    public QuestActionType getType() {
        return QuestActionType.valueOf(this.type);
    }
}
