package com.myplugin.lib.json.config.configs;

import com.google.gson.JsonArray;
import com.myplugin.SpigotDragonZ;
import com.myplugin.lib.Logger;
import com.myplugin.lib.json.config.configs.quests.Quest;
import com.myplugin.lib.json.config.configs.quests.SideQuest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QuestsConfig {

    public JsonArray main;
    public JsonArray side;

    public HashMap<Integer, Quest> getMainQuests() {
        final HashMap<Integer, Quest> quests = new HashMap<>();
        main.forEach((ele) -> {
            final Quest quest = SpigotDragonZ.INSTANCE.getGson().fromJson(ele.getAsJsonObject(), Quest.class);
            quests.put(quest.id, quest);
        });

        return quests;
    }

    public final HashMap<Integer, List<Quest>> getMainQuestsByPage() {
        final HashMap<Integer, List<Quest>> mainQuestsByPage = new HashMap<>();
        final List<Quest> mainQuests = new ArrayList<>(this.getMainQuests().values());
        int currentIteratingPage = 1;

        for (int i = 0; i <= mainQuests.size(); i++) {
            if (i >= 9) {
                currentIteratingPage++;
            }

            mainQuestsByPage.computeIfAbsent(currentIteratingPage, k -> new ArrayList<>());
            final List<Quest> mapList = mainQuestsByPage.get(currentIteratingPage);
            try {
                mapList.add(mainQuests.get(i));
                mainQuestsByPage.put(currentIteratingPage, mapList);
            } catch (final IndexOutOfBoundsException ignored) {
                Logger.debug("Finished loading quests!");
                break;
            }
        }

        Logger.debug("Successfully loaded a total of: " + mainQuestsByPage.size() + " quests with: " + mainQuestsByPage.keySet().size() + " pages");
        return mainQuestsByPage;
    }

    public HashMap<Integer, HashMap<Integer, Quest>> getSideQuests() {
        final HashMap<Integer, HashMap<Integer, Quest>> quests = new HashMap<>();
        this.side.forEach((ele) -> {
            final SideQuest sideQuest = SpigotDragonZ.INSTANCE.getGson().fromJson(ele.getAsJsonObject(), SideQuest.class);
            quests.put(sideQuest.branchId, sideQuest.getSideQuests());
        });
        return quests;
    }
}
