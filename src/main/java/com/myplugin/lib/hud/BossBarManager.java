package com.myplugin.lib.hud;

import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.json.data.player.PlayerData;
import com.myplugin.lib.json.data.PlayerDataManager;
import com.myplugin.lib.json.config.enums.Race;
import com.myplugin.lib.events.TriggerBossBarUpdate;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.myplugin.MyPlugin.ofString;

public class BossBarManager implements Listener {

    private final HashMap<UUID, BossBar> playerBars;
    private final PlayerDataManager manager;

    public BossBarManager(final MyPlugin plugin) {
        this.manager = plugin.getDataManager();
        this.playerBars = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerJoin(final PlayerJoinEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();
        final PlayerData data = this.manager.getPlayerData(uuid);
        if (this.playerBars.get(uuid) == null && data.getRace() != Race.NONE) {
            final BossBar bar = this.createBossBar(uuid);
            bar.addPlayer(e.getPlayer());
            this.playerBars.put(uuid, bar);
            Logger.debug("Loaded BossBar for joining player with UUID: " + uuid.toString());
        }
    }

    @EventHandler
    public void playerLeave(final PlayerQuitEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();
        if (this.playerBars.get(uuid) != null) {
            final BossBar bar = this.playerBars.get(uuid);
            bar.removeAll();
            this.playerBars.remove(uuid);
            Logger.debug("Cleared BossBar instance for player with UUID: " + uuid.toString());
        }
    }

    @EventHandler
    public void onBarUpdate(final TriggerBossBarUpdate e) {
        BossBar bar = this.playerBars.get(e.getUuid());
        final PlayerData data = e.getData();
        if (bar != null) {
            this.updateBossBar(data, bar);
            this.playerBars.replace(e.getUuid(), bar);
        } else Logger.warn("Trying to update BossBar when Player does not have one attached to them! Player UUID: " + e.getUuid().toString());
    }

    /**
     * Since player by default start with race NONE, and the plugin wont load a player bar
     * for a player without a Race, this method will be called once the user picks his/her race
     * that way they get a player bar without needing to relog
     * @param uuid
     */
    public void loadPlayerBar(final UUID uuid) {
        final Player p = Bukkit.getPlayer(uuid);
        if (this.playerBars.get(uuid) == null && p != null) {
            final BossBar bar = this.createBossBar(uuid);
            bar.addPlayer(p);
            this.playerBars.put(uuid, bar);
            Logger.debug("Successfully added BossBar for player with UUID: " + uuid.toString() + " (Loaded by loadPlayerBar)");
        }
    }

    public final BossBar createBossBar(final UUID uuid) {
        final PlayerData data = this.manager.getPlayerData(uuid);
        final BossBar bar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
        return this.updateBossBar(data, bar);
    }

    public final BossBar updateBossBar(final PlayerData data,
                                       final BossBar bar) {
        final int maxHealth = data.getMaxHealth();
        final int currentHealth = data.getCurrentHealth();
        Logger.debug("BossBar maxHealth: " + maxHealth);
        Logger.debug("BossBar currentHealth" + currentHealth);

        final float percentOf = MyPlugin.getPercentOf(currentHealth, maxHealth);
        Logger.debug("BossBar percent of health " + percentOf);
        bar.setProgress(percentOf);
        if (percentOf >= 0.90) {
            bar.setColor(BarColor.GREEN);
            bar.setTitle(ofString("&2&l" + Math.round(percentOf * 100) + "% Health"));
        } else if (percentOf >= 0.75) {
            bar.setColor(BarColor.GREEN);
            bar.setTitle(ofString("&a&l" + Math.round(percentOf * 100) + "% Health"));
        } else if (percentOf >= 0.60) {
            bar.setColor(BarColor.YELLOW);
            bar.setTitle(ofString("&6&l" + Math.round(percentOf * 100) + "% Health"));
        } else if (percentOf >= 0.45) {
            bar.setColor(BarColor.YELLOW);
            bar.setTitle(ofString("&e&l" + Math.round(percentOf * 100) + "% Health"));
        } else if (percentOf >= 0.25) {
            bar.setColor(BarColor.RED);
            bar.setTitle(ofString("&c&l" + Math.round(percentOf * 100) + "% Health"));
        } else {
            bar.setColor(BarColor.RED);
            bar.setTitle(ofString("&4&l" + Math.round(percentOf * 100) + "% Health"));
        }

        return bar;
    }
}
