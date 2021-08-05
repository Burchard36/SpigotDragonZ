package com.myplugin.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;
import com.myplugin.lib.config.ConfigPath;
import com.myplugin.lib.config.ConfigPerLevelPath;
import com.myplugin.lib.config.ConfigSkillPath;
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

    private transient int perSpStrInc;
    private transient int perSpMaxHpInc;
    private transient int perSpMaxKiInc;
    private transient int perSpKiPowInc;
    private transient int perSpDefInc;
    private transient int perSpStamInc;

    private transient int perLvlStrInc;
    private transient int perLvlMaxHpInc;
    private transient int perLvlMaxKiInc;
    private transient int perLvlKiPowInc;
    private transient int perLvlDefInc;
    private transient int perLvlStamInc;

    private transient int currentKi;
    private transient int currentStamina;
    private transient int currentHealth;


    public PlayerData(final JsonObject playerStats,
                      final JsonObject talentPoints,
                      final MyPlugin plugin,
                      final UUID uuid) {
        this.playerStats = playerStats;
        this.talentPoints = talentPoints;
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.uuid = uuid;
        this.setConfigValues();

        final JsonElement currentHp = this.playerStats.get("currentHealth");
        final JsonElement currentKi = this.playerStats.get("currentKi");
        final JsonElement currentHealth = this.playerStats.get("currentHealth");
    }

    @EventHandler
    public void onConfigUpdate(final TriggerConfigUpdate e) {
        this.config = plugin.getConfig();
        this.setConfigValues();
    }

    private void setConfigValues() {
        this.tpPerLevel = this.config.getInt(ConfigPath.TALENT_POINTS_PER_LEVEL.toString());
        this.maxStartingExp = this.config.getInt(ConfigPath.MAX_BASE_EXP.toString());
        this.expIncrease = this.config.getInt(ConfigPath.EXP_INCREASE_PER_LEVEL.toString());

        final Race playerRace = this.getPlayerRace();
        switch(playerRace) {
            case SAIYAN: {
                this.perSpStrInc = this.config.getInt(ConfigSkillPath.SAIYAN_STR_INCREASE.toString());
                this.perSpMaxHpInc = this.config.getInt(ConfigSkillPath.SAIYAN_HP_INCREASE.toString());
                this.perSpMaxKiInc = this.config.getInt(ConfigSkillPath.SAIYAN_MAX_KI_INCREASE.toString());
                this.perSpKiPowInc = this.config.getInt(ConfigSkillPath.SAIYAN_KI_POW_INCREASE.toString());
                this.perSpDefInc = this.config.getInt(ConfigSkillPath.SAIYAN_DEFENSE_INCREASE.toString());
                this.perSpStamInc = this.config.getInt(ConfigSkillPath.SAIYAN_STAM_INCREASE.toString());

                this.perLvlStrInc = this.config.getInt(ConfigPerLevelPath.SAIYAN_STR_INCREASE.toString());
                this.perLvlMaxHpInc = this.config.getInt(ConfigPerLevelPath.SAIYAN_HP_INCREASE.toString());
                this.perLvlMaxKiInc = this.config.getInt(ConfigPerLevelPath.SAIYAN_MAX_KI_INCREASE.toString());
                this.perLvlKiPowInc = this.config.getInt(ConfigPerLevelPath.SAIYAN_KI_POW_INCREASE.toString());
                this.perLvlDefInc = this.config.getInt(ConfigPerLevelPath.SAIYAN_DEFENSE_INCREASE.toString());
                this.perLvlStamInc = this.config.getInt(ConfigPerLevelPath.SAIYAN_STAM_INCREASE.toString());
            }

            case HALF_SAIYAN: {
                this.perSpStrInc = this.config.getInt(ConfigSkillPath.HALF_SAIYAN_STR_INCREASE.toString());
                this.perSpMaxHpInc = this.config.getInt(ConfigSkillPath.HALF_SAIYAN_HP_INCREASE.toString());
                this.perSpMaxKiInc = this.config.getInt(ConfigSkillPath.HALF_SAIYAN_MAX_KI_INCREASE.toString());
                this.perSpKiPowInc = this.config.getInt(ConfigSkillPath.HALF_SAIYAN_KI_POW_INCREASE.toString());
                this.perSpDefInc = this.config.getInt(ConfigSkillPath.HALF_SAIYAN_DEFENSE_INCREASE.toString());
                this.perSpStamInc = this.config.getInt(ConfigSkillPath.HALF_SAIYAN_STAM_INCREASE.toString());

                this.perLvlStrInc = this.config.getInt(ConfigPerLevelPath.HALF_SAIYAN_STR_INCREASE.toString());
                this.perLvlMaxHpInc = this.config.getInt(ConfigPerLevelPath.HALF_SAIYAN_HP_INCREASE.toString());
                this.perLvlMaxKiInc = this.config.getInt(ConfigPerLevelPath.HALF_SAIYAN_MAX_KI_INCREASE.toString());
                this.perLvlKiPowInc = this.config.getInt(ConfigPerLevelPath.HALF_SAIYAN_KI_POW_INCREASE.toString());
                this.perLvlDefInc = this.config.getInt(ConfigPerLevelPath.HALF_SAIYAN_DEFENSE_INCREASE.toString());
                this.perLvlStamInc = this.config.getInt(ConfigPerLevelPath.HALF_SAIYAN_STAM_INCREASE.toString());
            }
        }
    }

    /*
     * Everything below here is not passed to be serialized through google GSON
     * Think of these on a as instance basis
     */

    public final int getPlayerStrength() {
        final JsonElement strPointsSpent = this.talentPoints.get("strength");
        if (strPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return  (strPointsSpent.getAsInt() * this.perSpStrInc) + (playerLevel * this.perLvlStrInc);
    }

    public final int getPlayerMaxHealth() {
        final JsonElement hpPointsSpent = this.talentPoints.get("maxHealth");
        if (hpPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return (hpPointsSpent.getAsInt() * this.perSpMaxHpInc) + (playerLevel * this.perLvlMaxHpInc);
    }

    public final int getPlayerMaxKi() {
        final JsonElement kiPointsSpent = this.talentPoints.get("maxKi");
        if (kiPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return (kiPointsSpent.getAsInt() * this.perSpMaxKiInc) + (playerLevel * this.perLvlMaxKiInc);
    }

    public final int getPlayerDefense() {
        final JsonElement defPointsSpent = this.talentPoints.get("defense");
        if (defPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return (defPointsSpent.getAsInt() * this.perSpDefInc) + (playerLevel * this.perLvlDefInc);
    }

    public final int getPlayerKiPower() {
        final JsonElement kiPowPointsSpent = this.talentPoints.get("kiPower");
        if (kiPowPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return (kiPowPointsSpent.getAsInt() * this.perSpKiPowInc) + (playerLevel + this.perLvlKiPowInc);
    }

    public final int getPlayerMaxStamina() {
        final JsonElement stamPointsSpent = this.talentPoints.get("stamina");
        if (stamPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return (stamPointsSpent.getAsInt() * this.perSpStamInc) + (playerLevel + this.perLvlStamInc);
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
        final JsonElement element = this.talentPoints.get("talentPoints");
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
        }
        return currentPoints;
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

    public final void setPlayerRace(final Race race) {
        this.playerStats.remove("race");
        this.playerStats.addProperty("race", race.toString());
        Bukkit.getPluginManager().callEvent(new TriggerDataUpdate(this.uuid, this));
    }
}
