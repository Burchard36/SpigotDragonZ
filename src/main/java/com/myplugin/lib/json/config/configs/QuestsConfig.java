package com.myplugin.lib.json.config.configs;

import com.google.gson.JsonArray;
import com.myplugin.MyPlugin;
import com.myplugin.lib.json.config.configs.quests.Quest;
import com.myplugin.lib.json.config.configs.quests.SideQuest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestsConfig {

    public JsonArray main;
    public JsonArray side;

    public HashMap<Integer, Quest> getMainQuests() {
        final HashMap<Integer, Quest> quests = new HashMap<>();
        main.forEach((ele) -> {
            final Quest quest = MyPlugin.INSTANCE.getGson().fromJson(ele.getAsJsonObject(), Quest.class);
            quests.put(quest.id, quest);
        });

        return quests;
    }

    public HashMap<Integer, HashMap<Integer, Quest>> getSideQuests() {
        final HashMap<Integer, HashMap<Integer, Quest>> quests = new HashMap<>();
        this.side.forEach((ele) -> {
            final SideQuest sideQuest = MyPlugin.INSTANCE.getGson().fromJson(ele.getAsJsonObject(), SideQuest.class);
            quests.put(sideQuest.branchId, sideQuest.getSideQuests());
        });
        return quests;
    }
}
