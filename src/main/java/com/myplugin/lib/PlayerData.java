package com.myplugin.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;
import com.myplugin.lib.config.ConfigPath;
import com.myplugin.lib.config.ConfigPerLevelPath;
import com.myplugin.lib.config.ConfigSkillPath;
import com.myplugin.lib.dragonball.Race;
import com.myplugin.lib.events.TriggerBossBarUpdate;
import com.myplugin.lib.events.TriggerConfigUpdate;
import com.myplugin.lib.events.TriggerDataUpdate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

import static com.myplugin.MyPlugin.getPercentOf;

public class PlayerData implements Listener {

    /* Everything here is passed through JSON */
    public JsonObject playerStats;
    public JsonObject talentPoints;

    private transient MyPlugin plugin;
    private transient UUID uuid;
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
    }

    public void init(final MyPlugin plugin,
                     final UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.setConfigValues();
    }

    @EventHandler
    public void onConfigUpdate(final TriggerConfigUpdate e) {
        this.setConfigValues();
    }

    public void setConfigValues() {
        this.config = plugin.getConfig();
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
                Logger.debug("Loaded SAIYAN level and talent point increments");
                break;
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
                Logger.debug("Loaded HALF_SAIYAN level and talent point increments");
                break;
            }

            case NONE: {
                Logger.debug("Did not load variables because players race is NONE");
                break;
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
        Logger.debug("getPlayerMaxHealth #1 " + (this.perSpMaxHpInc));
        Logger.debug("getPlayerMaxHealth #2 " + (this.perLvlMaxHpInc));
        return (hpPointsSpent.getAsInt() * this.perSpMaxHpInc) + (playerLevel * this.perLvlMaxHpInc);
    }

    public final int getPlayerHealth() {
        final JsonElement health = this.playerStats.get("currentHealth");
        if (health == null) return -1;
        return health.getAsInt();
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

    public final int removeHealth(final int amt) {
        int healthLeft = this.getPlayerHealth() - amt;
        if (healthLeft < 0) healthLeft = 0;
        this.playerStats.remove("currentHealth");
        this.playerStats.addProperty("currentHealth", healthLeft);
        return healthLeft;
    }

    public void applyDamage(final int incomingDamage) {
        int damageDealt = incomingDamage - (this.getPlayerDefense() / 2);
        int healthLeft = this.removeHealth(damageDealt);
        if (healthLeft <= 0) {
            this.reset(true);
        }
        Bukkit.getPluginManager().callEvent(new TriggerBossBarUpdate(this.uuid, this));
    }

    public final void setPlayerRace(final Race race) {
        this.playerStats.remove("race");
        this.playerStats.addProperty("race", race.toString());
        this.setConfigValues();
        this.reset(false);
        this.triggerUpdate();
    }

    public final void reset(final boolean killPlayer) {
        this.playerStats.remove("currentHealth");
        this.playerStats.remove("currentKi");
        this.playerStats.remove("currentStamina");
        this.playerStats.addProperty("currentHealth", this.getPlayerMaxHealth());
        this.playerStats.addProperty("currentKi", this.getPlayerMaxKi());
        this.playerStats.addProperty("currentStamina", this.getPlayerMaxStamina());
        if (killPlayer) {
            final Player p = Bukkit.getPlayer(this.uuid);
            if (p != null) {
                p.setHealth(0D);
            }
        }
    }

    public final void triggerUpdate() {
        Bukkit.getPluginManager().callEvent(new TriggerDataUpdate(this.uuid, this));
    }
}
