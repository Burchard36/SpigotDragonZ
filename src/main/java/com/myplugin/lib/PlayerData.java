package com.myplugin.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;
import com.myplugin.lib.dragonball.Race;
import com.myplugin.lib.events.TriggerDataUpdate;
import org.bukkit.Bukkit;

import java.util.UUID;

import static com.myplugin.MyPlugin.getPercentOf;

public class PlayerData {

    /* Everything here is passed through JSON */
    public JsonObject playerStats;

    private final MyPlugin plugin;
    private final UUID uuid;
    public PlayerData(final JsonObject playerStats,
                      final MyPlugin plugin,
                      final UUID uuid) {
        this.playerStats = playerStats;
        this.plugin = plugin;
        this.uuid = uuid;
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

    public final int getTalentPoint() {
        final int tpPerLevel = this.plugin.getConfig().getInt("GameSettings.TalentPointsPerLevel");
        final int tpSpent = this.getTalentPointsSpent();
        final int level = this.getPlayerLevel();
        final JsonElement element = this.playerStats.get("talentPoints");
        if (element == null) return -1;

        int currentPoints = element.getAsInt();
        final int expectedTp = level * tpPerLevel;
        if (expectedTp < (tpSpent + currentPoints)) {
            final int tpToAdd = expectedTp - (tpSpent + currentPoints);
            currentPoints += tpToAdd;
            this.playerStats.remove("talentPoints");
            this.playerStats.addProperty("talentPoints", currentPoints);
            Bukkit.getPluginManager().callEvent(new TriggerDataUpdate(this.uuid, this));
            return currentPoints;
        } else return currentPoints;
    }

    public final int calculateMaxExperience() {
        int expIncrease = this.plugin.getConfig().getInt("GameSettings.LevelExpIncrease");
        int startingExp = this.plugin.getConfig().getInt("GameSettings.BaseMaxExp");
        for (int x = 0; x < this.getPlayerLevel(); x++) {
            startingExp += getPercentOf(startingExp, expIncrease);
        }
        return startingExp;
    }
}
