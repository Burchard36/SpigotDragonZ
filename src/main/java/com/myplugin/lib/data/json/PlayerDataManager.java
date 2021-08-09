package com.myplugin.lib.data.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.data.json.config.enums.ConfigPath;
import com.myplugin.lib.data.json.config.enums.PlayerProperty;
import com.myplugin.lib.data.json.config.enums.Race;
import com.myplugin.lib.events.TriggerConfigUpdate;
import com.myplugin.lib.events.TriggerDataUpdate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.myplugin.MyPlugin.ofString;

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
        this.gson = this.plugin.getGson();
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
        savePlayerAsync(e.getUUID(), e.getData());
        Logger.debug("A DataUpdate was triggered for player with UUID: &b" + e.getUUID().toString());
    }

    @EventHandler
    public void onTriggerConfigUpdate(final TriggerConfigUpdate e) {
        this.setConfigValues();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        this.loadPlayerData(p.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline()) {
                    this.cancel();
                    return;
                }
                final PlayerData playerData = getPlayerData(p.getUniqueId());
                if (playerData.getPlayerRace() == Race.NONE) {
                    p.sendMessage(Component.text(ofString("&eYou have not selected your race yet! Type &6/race&e to select your race!")));
                } else this.cancel();
            }
        }.runTaskTimer(this.plugin, 0, (20 * 60) * 3);
    }

    private void setConfigValues() {
        this.saveTime = this.plugin.getConfig().getInt(ConfigPath.SAVE_CACHE_INTERVAL.toString());


    }

    public final void savePlayerAsync(final UUID uuidOfPlayer, final PlayerData data) {
        this.playerCache.replace(uuidOfPlayer, data);
        new BukkitRunnable() {
            @Override
            public void run() {
                savePlayer(uuidOfPlayer, data);
            }
        }.runTaskAsynchronously(this.plugin);
    }

    public final void savePlayer(final UUID uuidOfPlayer, final PlayerData data) {
        this.playerCache.replace(uuidOfPlayer, data);
        final File dataFile = new File(plugin.getDataFolder(), "data/players/" + uuidOfPlayer.toString() + ".json");
        try {
            final FileWriter writer = new FileWriter(dataFile);
            gson.toJson(data, writer);
            writer.flush();
            writer.close();
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
            if (!file.exists()) {
                if (file.createNewFile()) {
                    final PlayerData data = this.getDefaultData(uuidOfPlayer);
                    final FileWriter writer = new FileWriter(file);
                    gson.toJson(data, writer);
                    this.playerCache.put(uuidOfPlayer, data);
                    Logger.log("Successfully created PlayerData file for player with UUID: &e" + uuidOfPlayer.toString());
                    writer.close();
                } else Logger.warn("Could not create new file for UUID: &b" + uuidOfPlayer.toString());
            } else {
                final Reader reader = Files.newBufferedReader(file.toPath());
                final PlayerData playerData = gson.fromJson(reader, PlayerData.class);
                playerData.init(this.plugin, uuidOfPlayer);
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
        playerStats.addProperty(PlayerProperty.RACE.toString(), Race.NONE.toString());
        playerStats.addProperty(PlayerProperty.CURRENT_KI.toString(), 0);
        playerStats.addProperty(PlayerProperty.CURRENT_HEALTH.toString(), 0);
        playerStats.addProperty(PlayerProperty.CURRENT_STAMINA.toString(), 0);
        playerStats.addProperty(PlayerProperty.LEVEL.toString(), 1);
        playerStats.addProperty(PlayerProperty.CURRENT_EXP.toString(), 0);
        final JsonObject talentPoints = new JsonObject();
        talentPoints.addProperty(PlayerProperty.TALENT_POINTS_SPENT.toString(), 0);
        talentPoints.addProperty(PlayerProperty.TALENT_POINTS.toString(), 0);
        talentPoints.addProperty(PlayerProperty.STRENGTH.toString(), 0);
        talentPoints.addProperty(PlayerProperty.MAX_KI.toString(), 0);
        talentPoints.addProperty(PlayerProperty.MAX_HEALTH.toString(), 0);
        talentPoints.addProperty(PlayerProperty.DEFENSE.toString(), 0);
        talentPoints.addProperty(PlayerProperty.KI_POWER.toString(), 0);
        talentPoints.addProperty(PlayerProperty.MAX_STAMINA.toString(), 0);

        return new PlayerData(playerStats, talentPoints, this.plugin, uuid);
    }
}
