package com.burchard36.plugin.events;

import com.burchard36.plugin.SpigotDragonZ;
import com.burchard36.plugin.api.events.PlayerDataLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final SpigotDragonZ plugin;

    public PlayerJoin(final SpigotDragonZ pluginInstance) {
        this.plugin = pluginInstance;
    }

    /**
     * Use this event that way we 100% know when the player's data has loaded in
     * @param e PlayerDataLoadedEvent, provided by BukkitApi
     */
    @EventHandler
    public void onPlayerJoin(final PlayerDataLoadedEvent e) {

    }
}
