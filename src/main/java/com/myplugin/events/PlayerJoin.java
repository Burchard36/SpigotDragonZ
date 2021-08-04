package com.myplugin.events;

import com.myplugin.MyPlugin;
import com.myplugin.lib.PlayerData;
import com.myplugin.lib.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final PlayerDataManager dataManager;

    public PlayerJoin(final MyPlugin plugin) {
        this.dataManager = plugin.getDataManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        this.dataManager.loadPlayerData(p.getUniqueId());
        final PlayerData playerData = this.dataManager.getPlayerData(p.getUniqueId());
        Bukkit.getLogger().info("Player Strength gotten " + playerData.getPlayerStrength());
    }
}
