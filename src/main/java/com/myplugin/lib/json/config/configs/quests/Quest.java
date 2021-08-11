package com.myplugin.lib.json.config.configs.quests;

import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;

public class Quest {

    public int id;
    public int previous;
    public String name;
    public String description;
    public JsonObject action;
    public JsonObject rewards;

    public QuestReward getRewards() {
        return MyPlugin.INSTANCE.getGson().fromJson(this.rewards, QuestReward.class);
    }

    public QuestAction getAction() {
        return MyPlugin.INSTANCE.getGson().fromJson(this.action, QuestAction.class);
    }
}
