package com.myplugin.lib.json.config;

import com.myplugin.SpigotDragonZ;
import com.myplugin.lib.events.TriggerConfigUpdate;
import com.myplugin.lib.json.config.configs.QuestsConfig;
import com.myplugin.lib.json.config.configs.quests.Quest;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

public class QuestManager {

    private final SpigotDragonZ plugin;
    private HashMap<Integer, Quest> mainQuests;
    private HashMap<Integer, HashMap<Integer, Quest>> sideQuests;

    public QuestManager(final SpigotDragonZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConfigReload(final TriggerConfigUpdate e) {

    }

    private void setConfigValues() {
        final QuestsConfig questsConfig = this.plugin.getConfigManager().getQuestsConfig();

    }
}
