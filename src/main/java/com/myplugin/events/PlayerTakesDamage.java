package com.myplugin.events;

import com.myplugin.MyPlugin;
import com.myplugin.lib.PlayerData;
import com.myplugin.lib.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerTakesDamage implements Listener {

    private final MyPlugin plugin;
    private final PlayerDataManager manager;

    public PlayerTakesDamage(final MyPlugin plugin) {
        this.plugin = plugin;
        this.manager = this.plugin.getDataManager();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onHit(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            final PlayerData data = this.manager.getPlayerData(player.getUniqueId());
            data.applyDamage((int)e.getDamage());
        }
    }
}
