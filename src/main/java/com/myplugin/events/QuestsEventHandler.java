package com.myplugin.events;

import com.myplugin.SpigotDragonZ;
import com.myplugin.lib.json.config.configs.quests.Quest;
import com.myplugin.lib.json.data.player.PlayerData;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class QuestsEventHandler implements Listener {

    final SpigotDragonZ plugin;

    public QuestsEventHandler(final SpigotDragonZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void mobDeathEvent(final EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            final Player p = e.getEntity().getKiller();
            final PlayerData killerData = this.plugin.getDataManager().getPlayerData(p.getUniqueId());

            if (killerData.playerCurrentQuests.hasActiveMainQuest() &&
                !killerData.playerCurrentQuests.hasActiveSideQuests()) return;

            final int currentMainQuestInt = killerData.playerCurrentQuests.activeMainQuest;
            final ArrayList<Integer> activeSideQuests = killerData.playerCurrentQuests.activeSideQuests;

            if (!activeSideQuests.isEmpty()) {

            } else if (currentMainQuestInt > -1) {
                final Quest quest = this.plugin.getConfigManager().getQuestsConfig().getMainQuest()
            }

            final NamespacedKey searchKey = new NamespacedKey(this.plugin, "mob_id");
            final String mobId = e.getEntity().getPersistentDataContainer().get(searchKey, PersistentDataType.STRING);
            if (mobId == null) return;


        }
    }
}
