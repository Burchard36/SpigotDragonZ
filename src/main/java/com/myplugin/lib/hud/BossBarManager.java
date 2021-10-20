package com.myplugin.lib.hud;

import com.myplugin.SpigotDragonZ;
import com.myplugin.lib.Logger;
import com.myplugin.lib.json.data.player.PlayerData;
import com.myplugin.lib.json.data.PlayerDataManager;
import com.myplugin.lib.events.TriggerBossBarUpdate;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.myplugin.SpigotDragonZ.ofString;

public class BossBarManager implements Listener {

    private final HashMap<UUID, BossBar> playerBars;
    private final PlayerDataManager manager;

    public BossBarManager(final SpigotDragonZ plugin) {
        this.manager = plugin.getDataManager();
        this.playerBars = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        } else {
            bar = this.createBossBar(data);
            bar.addPlayer(data.getPlayer());
            this.playerBars.put(e.getUuid(), bar);
            Logger.debug("Loaded BossBar for joining player with UUID: " + e.getUuid().toString());
        }
    }

    public final BossBar createBossBar(final PlayerData data) {
        final BossBar bar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
        return this.updateBossBar(data, bar);
    }

    public final BossBar updateBossBar(final PlayerData data,
                                       final BossBar bar) {
        final int maxHealth = data.getMaxHealth();
        final int currentHealth = data.getCurrentHealth();
        Logger.debug("BossBar maxHealth: " + maxHealth);
        Logger.debug("BossBar currentHealth" + currentHealth);

        final float percentOf = SpigotDragonZ.getPercentOf(currentHealth, maxHealth);
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
