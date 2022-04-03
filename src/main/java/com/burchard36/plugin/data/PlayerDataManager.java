package com.burchard36.plugin.data;

import com.burchard36.Logger;
import com.burchard36.json.PluginDataMap;
import com.burchard36.plugin.SpigotDragonZ;
import com.burchard36.plugin.api.events.PlayerDataLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.UUID;

public class PlayerDataManager implements Listener {

    private final SpigotDragonZ plugin;
    private final PluginDataMap dataMap;

    public PlayerDataManager(final SpigotDragonZ pluginInstance) {
        this.plugin = pluginInstance;
        this.dataMap = new PluginDataMap();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final AsyncPlayerPreLoginEvent e) {

        final UUID uuid = e.getUniqueId();
        Logger.log("Attempting to load player data for UUID: " + uuid.toString());
        this.loadPlayerData(uuid);
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().getPluginManager().callEvent(new PlayerDataLoadedEvent(uuid, PlayerDataManager.this.getPlayerData(uuid)));
            }
        }.runTaskLater(this.plugin, 1L); // call this event back onto the main thread here
    }

    /**
     * Loads player data using a specified ID
     *
     * if said the data file does not exist, create a new one with default values
     *
     * this method should typically only be called on a join event, eg PlayerJoinEvent
     *
     * @param uuid UUID of player to use
     */
    public final void loadPlayerData(final UUID uuid) {
        if (this.dataMap.getDataFile(uuid.toString()) != null) return;

        final File playerDataFile = new File(this.plugin.getDataFolder(), "/data/" + uuid.toString() + ".json");

        if (playerDataFile.mkdirs()) {
            Logger.log("Created /data directory");
        }

        if (!playerDataFile.exists()) {
            Logger.log("Data did not exist for UUID: " + uuid.toString() + ". Creating new file with default values...");
            this.dataMap.createDataFile(new PlayerData(playerDataFile, this.plugin.getConfigManager()));
            this.loadPlayerData(uuid); // call event again to load the data file to cache
            return;
        }

        Logger.log("Loaded player data for player with UUID: " + uuid.toString());
        this.dataMap.loadDataFile(uuid.toString(), new PlayerData(playerDataFile, this.plugin.getConfigManager()));
    }

    /**
     * Gets a LOADED player data file from cache, null if the player data is not currently loaded
     * @param uuid UUID of player to grab
     * @return PlayerData instance, null if non found
     */
    public final PlayerData getPlayerData(final UUID uuid) {
        return (PlayerData) this.dataMap.getDataFile(uuid.toString());
    }

    /**
     * Saves all player data, I mean what else do you want me to put here? It's literally fucking called saveData
     */
    public final void saveData() {
        this.dataMap.saveAll();
    }
}
