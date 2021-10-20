package com.myplugin.lib.json.config.configs.quests;

import com.google.gson.JsonArray;
import com.myplugin.SpigotDragonZ;

import java.util.HashMap;

public class SideQuest {

    public int branchId;
    public JsonArray quests;

    public HashMap<Integer, Quest> getSideQuests() {
        final HashMap<Integer, Quest> quests = new HashMap<>();
        this.quests.forEach((ele) -> {
            final Quest quest = SpigotDragonZ.INSTANCE.getGson().fromJson(ele.getAsJsonObject(), Quest.class);
            quests.put(quest.id, quest);
        });
        return quests;
    }
}
