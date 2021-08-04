package com.myplugin.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.myplugin.MyPlugin;
import com.myplugin.lib.dragonball.Race;
import com.myplugin.lib.events.TriggerConfigUpdate;
import com.myplugin.lib.events.TriggerDataUpdate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

import static com.myplugin.MyPlugin.getPercentOf;

public class PlayerData implements Listener {

    /* Everything here is passed through JSON */
    public JsonObject playerStats;
    public JsonObject talentPoints;

    private transient final MyPlugin plugin;
    private transient final UUID uuid;
    private transient FileConfiguration config;

    private transient int tpPerLevel;
    private transient int maxStartingExp;
    private transient int expIncrease;

    public PlayerData(final JsonObject playerStats,
                      final MyPlugin plugin,
                      final UUID uuid) {
        this.playerStats = playerStats;
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.uuid = uuid;
        this.setConfigValues();
    }

    @EventHandler
    public void onConfigUpdate(final TriggerConfigUpdate e) {
        this.config = plugin.getConfig();
        this.setConfigValues();
    }

    private void setConfigValues() {
        this.tpPerLevel = this.plugin.getConfig().getInt(ConfigPath.TALENT_POINTS_PER_LEVEL.toString());
        this.maxStartingExp = this.plugin.getConfig().getInt(ConfigPath.MAX_BASE_EXP.toString());
        this.expIncrease = this.plugin.getConfig().getInt(ConfigPath.EXP_INCREASE_PER_LEVEL.toString());
    }

    /*
     * Everything below here is not passed to be serialized through google GSON
     * Think of these on a as instance basis
     */

    public final int getPlayerStrength() {
        final JsonElement element = this.playerStats.get("strength");
        if (element == null) return -1;
        return element.getAsInt();
    }

    public final Race getPlayerRace() {
        final JsonElement element = this.playerStats.get("race");
        if (element == null) return Race.NONE;
        else return Race.valueOf(element.getAsString());
    }

    public final int getPlayerLevel() {
        final JsonElement element = this.playerStats.get("level");
        if (element == null) return -1;
        else return element.getAsInt();
    }

    public final int getPlayerExp() {
        final JsonElement element = this.playerStats.get("currentExp");
        if (element == null) return -1;
        else return element.getAsInt();
    }

    public final int getTalentPointsSpent() {
        final JsonElement element = this.playerStats.get("talentPoints");
        if (element == null) return -1;
        else return element.getAsInt();
    }

    public final int getTalentPoints() {
        final int tpSpent = this.getTalentPointsSpent();
        final int level = this.getPlayerLevel();
        final JsonElement element = this.playerStats.get("talentPoints");
        if (element == null) return -1;

        int currentPoints = element.getAsInt();
        final int expectedTp = level * this.tpPerLevel;
        if (expectedTp < (tpSpent + currentPoints)) {
            final int tpToAdd = expectedTp - (tpSpent + currentPoints);
            currentPoints += tpToAdd;
            this.playerStats.remove("talentPoints");
            this.playerStats.addProperty("talentPoints", currentPoints);
            Bukkit.getPluginManager().callEvent(new TriggerDataUpdate(this.uuid, this));
            return currentPoints;
        } else return currentPoints;
    }

    public final int getMaxExperience() {
        int maxExp = 0;
        for (int x = 1; x <= this.getPlayerLevel(); x++) {
            if (x == 1) {
                maxExp += getPercentOf(this.maxStartingExp, this.expIncrease);
            } else maxExp += getPercentOf(maxExp, this.expIncrease);
        }
        return maxExp;
    }
}
