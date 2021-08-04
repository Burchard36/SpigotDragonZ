package com.myplugin.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;
import com.myplugin.lib.dragonball.Race;
import com.myplugin.lib.events.TriggerDataUpdate;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager implements Listener {

    private final MyPlugin plugin;
    private final Gson gson;
    private final HashMap<UUID, PlayerData> playerCache;
    private final BukkitTask autoSaveTask;


    public PlayerDataManager(final MyPlugin plugin) {
        this.plugin = plugin;
        final int saveTime = this.plugin.getConfig().getInt("DataSettings.SaveCacheInterval");
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.playerCache = new HashMap<>();
        this.autoSaveTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getLogger().info("Automatically saving player data in cache");
                saveAllCache();
                Bukkit.getLogger().info("Successfully saved all player data in cache");
            }
        }.runTaskTimerAsynchronously(this.plugin, 0, (20 * 60) * saveTime);
    }

    @EventHandler
    public void onTriggerUpdate(final TriggerDataUpdate e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                savePlayer(e.getUUID(), e.getData());
                Bukkit.getLogger().info("An updated was triggered for UUID: " + e.getUUID());
            }
        }.runTaskAsynchronously(this.plugin);
    }

    public final void savePlayer(final UUID uuidOfPlayer, final PlayerData data) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final File dataFile = new File(plugin.getDataFolder(), "data/players/" + uuidOfPlayer.toString() + ".json");
                try {
                    final FileWriter writer = new FileWriter(dataFile);
                    gson.toJson(data, writer);
                    writer.close();
                    playerCache.put(uuidOfPlayer, data);
                    Bukkit.getLogger().info("Saved data for UUID: " + uuidOfPlayer.toString());
                } catch (final IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }

    public void loadPlayerData(final UUID uuidOfPlayer) {
        this.validateDirs();
        if (this.playerCache.containsKey(uuidOfPlayer)) return;
        final File file = new File(this.plugin.getDataFolder(), "data/players/" + uuidOfPlayer.toString() + ".json");
        try {
            if (!file.exists()) if (file.createNewFile()) {
                final PlayerData data = this.getDefaultData(uuidOfPlayer);
                final FileWriter writer = new FileWriter(file);
                gson.toJson(data, writer);
                this.playerCache.put(uuidOfPlayer, data);
                Bukkit.getLogger().info("Created player data file for UUID: " + uuidOfPlayer.toString());
                writer.close();
            } else {
                final Reader reader = Files.newBufferedReader(Paths.get(file.toURI()));
                final PlayerData playerData = gson.fromJson(reader, PlayerData.class);
                this.playerCache.put(uuidOfPlayer, playerData);
            }
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeRunnable() {
        this.autoSaveTask.cancel();
        HandlerList.unregisterAll(this);
    }

    private void validateDirs() {
        final File file = new File(this.plugin.getDataFolder(), "data/players");
        if (!file.exists()) if (file.mkdirs()) {
            Bukkit.getLogger().info("Successfully generated data/players folder!");
        }
    }

    private PlayerData getDefaultData(final UUID uuid) {
        JsonObject obj = new JsonObject();
        obj.addProperty("strength", 1);
        obj.addProperty("race", Race.NONE.toString());
        obj.addProperty("currentKi", 50);
        obj.addProperty("maxKi", 100);
        obj.addProperty("talentPoints", 0);
        obj.addProperty("totalTalentPointsSpent", 0);
        obj.addProperty("level", 1);
        obj.addProperty("currentExp", 0);
        return new PlayerData(obj, this.plugin, uuid);
    }

    public void saveAllCache() {
        this.playerCache.forEach(this::savePlayer);
    }

    public final PlayerData getPlayerData(final UUID uuidOfPlayer) {
        return this.playerCache.get(uuidOfPlayer);
    }

    public final void updatePlayer(final UUID uuidOfPlayer, final PlayerData data) {
        this.playerCache.put(uuidOfPlayer, data);
        this.savePlayer(uuidOfPlayer, data);
    }
}
