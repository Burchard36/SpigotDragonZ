package com.myplugin.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;
import com.myplugin.lib.config.ConfigPath;
import com.myplugin.lib.dragonball.Race;
import com.myplugin.lib.events.TriggerConfigUpdate;
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
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerDataManager implements Listener {

    private final MyPlugin plugin;
    private final Gson gson;
    private final HashMap<UUID, PlayerData> playerCache;
    private final BukkitTask autoSaveTask;

    private int saveTime;

    public PlayerDataManager(final MyPlugin plugin) {
        this.plugin = plugin;
        this.setConfigValues();
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.playerCache = new HashMap<>();
        this.autoSaveTask = new BukkitRunnable() {
            @Override
            public void run() {
                final Date startTime = new Date();
                Logger.debug("Running Auto-Save runner for all cached players. . .");
                saveAllCache(true);
                final Date endTime = new Date();
                long diff = endTime.getTime() - startTime.getTime();
                long milli = TimeUnit.MILLISECONDS.toMillis(diff);
                int seconds = (int) milli / 1000;
                if (seconds != 0) {
                    milli = milli % (seconds * 1000);
                }

                Logger.log("Successfully Auto-Saved data for cached players (Completed in " + seconds + "." + milli + " seconds!)");
            }
        }.runTaskTimerAsynchronously(this.plugin, 0, (20 * 60) * this.saveTime);
    }

    @EventHandler
    public void onTriggerUpdate(final TriggerDataUpdate e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                savePlayerAsync(e.getUUID(), e.getData());
                Logger.debug("A DataUpdate was triggered for player with UUID: &b" + e.getUUID().toString());
            }
        }.runTaskAsynchronously(this.plugin);
    }

    @EventHandler
    public void onTriggerConfigUpdate(final TriggerConfigUpdate e) {
        this.setConfigValues();
    }

    private void setConfigValues() {
        this.saveTime = this.plugin.getConfig().getInt(ConfigPath.SAVE_CACHE_INTERVAL.toString());
    }

    public final void savePlayerAsync(final UUID uuidOfPlayer, final PlayerData data) {
        new BukkitRunnable() {
            @Override
            public void run() {
                savePlayer(uuidOfPlayer, data);
            }
        }.runTaskAsynchronously(this.plugin);
    }

    public final void savePlayer(final UUID uuidOfPlayer, final PlayerData data) {
        final File dataFile = new File(plugin.getDataFolder(), "data/players/" + uuidOfPlayer.toString() + ".json");
        try {
            final FileWriter writer = new FileWriter(dataFile);
            gson.toJson(data, writer);
            writer.close();
            playerCache.put(uuidOfPlayer, data);
            Logger.debug("Successfully saved data for player with UUID: &b" + uuidOfPlayer.toString());
        } catch (final IOException ex) {
            Logger.error("An error occurred when calling PlayerDataManager#savePlayer. Please contact a developer!");
            ex.printStackTrace();
        }
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
                Logger.log("Successfully created PlayerData file for player with UUID: &e" + uuidOfPlayer.toString());
                writer.close();
            } else {
                final Reader reader = Files.newBufferedReader(Paths.get(file.toURI()));
                final PlayerData playerData = gson.fromJson(reader, PlayerData.class);
                this.playerCache.put(uuidOfPlayer, playerData);
                Logger.debug("Successfully loaded PlayerData for UUID: &b" + uuidOfPlayer.toString());
            }
        } catch (final IOException ex) {
            Logger.error("Error when calling PlayerDataManager#loadPlayerData. Please contact a developer!");
            ex.printStackTrace();
        }
    }

    public void closeRunnable() {
        Logger.log("Shutting down PlayerDataManager Listener and cancelling Runnable tasks...");
        this.autoSaveTask.cancel();
        HandlerList.unregisterAll(this);
    }

    private void validateDirs() {
        final File file = new File(this.plugin.getDataFolder(), "data/players");
        if (!file.exists()) if (file.mkdirs()) {
            Logger.log("Successfully created directory for saving PlayerData json files!");
        }
    }

    public void saveAllCache(final boolean runAsync) {
        Logger.log("Saving cache to data file for all cached players. . .");
        if (runAsync) {
            this.playerCache.forEach(this::savePlayerAsync);
        } else {
            this.playerCache.forEach(this::savePlayer);
        }
    }

    public final PlayerData getPlayerData(final UUID uuidOfPlayer) {
        return this.playerCache.get(uuidOfPlayer);
    }

    private PlayerData getDefaultData(final UUID uuid) {
        final JsonObject playerStats = new JsonObject();
        playerStats.addProperty("race", Race.NONE.toString());
        playerStats.addProperty("currentKi", 0);
        playerStats.addProperty("currentHealth", 0);
        playerStats.addProperty("currentStamina", 0);
        playerStats.addProperty("level", 1);
        playerStats.addProperty("currentExp", 0);
        final JsonObject talentPoints = new JsonObject();
        talentPoints.addProperty("totalTalentPointsSpent", 0);
        talentPoints.addProperty("talentPoints", 0);
        talentPoints.addProperty("strength", 0);
        talentPoints.addProperty("maxKi", 0);
        talentPoints.addProperty("maxHealth", 0);
        talentPoints.addProperty("defense", 0);
        talentPoints.addProperty("kiPower", 0);
        talentPoints.addProperty("stamina", 0);

        return new PlayerData(playerStats, talentPoints, this.plugin, uuid);
    }
}
