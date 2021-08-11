package com.myplugin.lib;

import com.myplugin.MyPlugin;
import com.myplugin.lib.events.TriggerConfigUpdate;
import com.myplugin.lib.json.config.configs.QuestsConfig;
import com.myplugin.lib.json.config.configs.quests.Quest;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

public class QuestManager {

    private final MyPlugin plugin;
    private HashMap<Integer, Quest> mainQuests;
    private HashMap<Integer, HashMap<Integer, Quest>> sideQuests;

    public QuestManager(final MyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConfigReload(final TriggerConfigUpdate e) {

    }

    private void setConfigValues() {
        final QuestsConfig questsConfig = this.plugin.getConfigManager().getQuestsConfig();

    }
}
