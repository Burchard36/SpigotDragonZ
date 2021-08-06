package com.myplugin.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;
import com.myplugin.lib.config.ConfigBaseStats;
import com.myplugin.lib.config.ConfigPath;
import com.myplugin.lib.config.ConfigPerLevelPath;
import com.myplugin.lib.config.ConfigSkillPath;
import com.myplugin.lib.dragonball.PlayerProperty;
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

import static com.myplugin.MyPlugin.getDoublePercentOf;
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

    private transient int defaultMaxHp;
    private transient int defaultMaxStam;
    private transient int defaultMaxKi;
    private transient int defaultStrength;
    private transient int defaultDefense;
    private transient int defaultKiPow;

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

                this.defaultStrength = this.config.getInt(ConfigBaseStats.SAIYAN_DEFAULT_DEFENSE.toString());
                this.defaultDefense = this.config.getInt(ConfigBaseStats.SAIYAN_DEFAULT_DEFENSE.toString());
                this.defaultKiPow = this.config.getInt(ConfigBaseStats.SAIYAN_DEFAULT_KI_POWER.toString());
                this.defaultMaxHp = this.config.getInt(ConfigBaseStats.SAIYAN_DEFAULT_MAX_HP.toString());
                this.defaultMaxKi = this.config.getInt(ConfigBaseStats.SAIYAN_DEFAULT_MAX_KI.toString());
                this.defaultMaxStam = this.config.getInt(ConfigBaseStats.SAIYAN_DEFAULT_MAX_STAM.toString());

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
        final JsonElement strPointsSpent = this.talentPoints.get(PlayerProperty.STRENGTH.toString());
        if (strPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return  (strPointsSpent.getAsInt() * this.perSpStrInc) + (playerLevel * this.perLvlStrInc);
    }

    public final int getPlayerMaxHealth() {
        final JsonElement hpPointsSpent = this.talentPoints.get(PlayerProperty.MAX_HEALTH.toString());
        if (hpPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        Logger.debug("getPlayerMaxHealth #1 " + (this.perSpMaxHpInc));
        Logger.debug("getPlayerMaxHealth #2 " + (this.perLvlMaxHpInc));
        return (hpPointsSpent.getAsInt() * this.perSpMaxHpInc) + (playerLevel * this.perLvlMaxHpInc);
    }

    public final int getPlayerHealth() {
        final JsonElement health = this.playerStats.get(PlayerProperty.CURRENT_HEALTH.toString());
        if (health == null) return -1;
        return health.getAsInt();
    }

    public final int getPlayerKi() {
        final JsonElement currentKi = this.playerStats.get(PlayerProperty.CURRENT_KI.toString());
        if (currentKi == null) return -1;
        return currentKi.getAsInt();
    }

    public final int getPlayerMaxKi() {
        final JsonElement kiPointsSpent = this.talentPoints.get(PlayerProperty.MAX_KI.toString());
        if (kiPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return (kiPointsSpent.getAsInt() * this.perSpMaxKiInc) + (playerLevel * this.perLvlMaxKiInc);
    }

    public final int getPlayerDefense() {
        final JsonElement defPointsSpent = this.talentPoints.get(PlayerProperty.DEFENSE.toString());
        if (defPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return (defPointsSpent.getAsInt() * this.perSpDefInc) + (playerLevel * this.perLvlDefInc);
    }

    public final int getPlayerKiPower() {
        final JsonElement kiPowPointsSpent = this.talentPoints.get(PlayerProperty.KI_POWER.toString());
        if (kiPowPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return (kiPowPointsSpent.getAsInt() * this.perSpKiPowInc) + (playerLevel + this.perLvlKiPowInc);
    }

    public final int getPlayerMaxStamina() {
        final JsonElement stamPointsSpent = this.talentPoints.get(PlayerProperty.MAX_STAMINA.toString());
        if (stamPointsSpent == null) return -1;
        final int playerLevel = this.getPlayerLevel();
        return (stamPointsSpent.getAsInt() * this.perSpStamInc) + (playerLevel + this.perLvlStamInc);
    }

    public final int getPlayerStamina() {
        final JsonElement stamina = this.playerStats.get(PlayerProperty.CURRENT_STAMINA.toString());
        if (stamina == null) return -1;
        return stamina.getAsInt();
    }

    public final Race getPlayerRace() {
        final JsonElement element = this.playerStats.get(PlayerProperty.RACE.toString());
        if (element == null) return Race.NONE;
        else return Race.valueOf(element.getAsString());
    }

    public final int getPlayerLevel() {
        final JsonElement element = this.playerStats.get(PlayerProperty.LEVEL.toString());
        if (element == null) return -1;
        else return element.getAsInt();
    }

    public final int getPlayerExp() {
        final JsonElement element = this.playerStats.get(PlayerProperty.CURRENT_EXP.toString());
        if (element == null) return -1;
        else return element.getAsInt();
    }

    public final int getTalentPointsSpent() {
        final JsonElement element = this.talentPoints.get(PlayerProperty.TALENT_POINTS_SPENT.toString());
        if (element == null) return -1;
        else return element.getAsInt();
    }

    public final int getTalentPoints() {
        final int tpSpent = this.getTalentPointsSpent();
        final int level = this.getPlayerLevel();
        final JsonElement talentPoints = this.playerStats.get(PlayerProperty.TALENT_POINTS.toString());
        if (talentPoints == null) return -1;

        int currentPoints = talentPoints.getAsInt();
        final int expectedTp = level * this.tpPerLevel;
        if (expectedTp < (tpSpent + currentPoints)) {
            final int tpToAdd = expectedTp - (tpSpent + currentPoints);
            currentPoints += tpToAdd;
            this.playerStats.remove(PlayerProperty.TALENT_POINTS.toString());
            this.playerStats.addProperty(PlayerProperty.TALENT_POINTS.toString(), currentPoints);
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
        this.playerStats.remove(PlayerProperty.CURRENT_HEALTH.toString());
        this.playerStats.addProperty(PlayerProperty.CURRENT_HEALTH.toString(), healthLeft);
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

    public final void setPlayerStrength(final int strength) {
        this.talentPoints.remove(PlayerProperty.STRENGTH.toString());
        this.talentPoints.addProperty(PlayerProperty.STRENGTH.toString(), strength);
    }

    public final void addPlayerStrength(final int amtToAdd) {
        int current = this.getPlayerStrength();
        current += amtToAdd;
        this.setPlayerStrength(current);
    }

    public final void setPlayerMaxHealth(final int health) {
        this.talentPoints.remove(PlayerProperty.MAX_HEALTH.toString());
        this.talentPoints.addProperty(PlayerProperty.MAX_HEALTH.toString(), health);
    }

    public final void addPlayerMaxHealth(final int amtToAdd) {
        int current = this.getPlayerMaxHealth() + amtToAdd;
        this.setPlayerMaxHealth(current);
    }

    public final void setPlayerHealth(final int health) {
        this.playerStats.remove(PlayerProperty.CURRENT_HEALTH.toString());
        this.playerStats.addProperty(PlayerProperty.CURRENT_HEALTH.toString(), health);
    }

    public final void addPlayerHealth(final int health) {
        int current = this.getPlayerHealth() + health;
        this.setPlayerHealth(current);
    }

    public final void setPlayerMaxKi(final int maxKi) {
        this.talentPoints.remove(PlayerProperty.MAX_KI.toString());
        this.talentPoints.addProperty(PlayerProperty.MAX_HEALTH.toString(), maxKi);
    }

    public final void addPlayerMaxKi(final int amount) {
        int current = this.getPlayerMaxKi() + amount;
        this.setPlayerMaxKi(current);
    }

    public final void setPlayerKi(final int maxKi) {
        this.playerStats.remove(PlayerProperty.CURRENT_KI.toString());
        this.playerStats.addProperty(PlayerProperty.MAX_KI.toString(), maxKi);
    }

    public final void addPlayerKi(final int amount) {
        int current = this.getPlayerKi() + amount;
        this.setPlayerKi(current);
    }

    public final void setPlayerKiPower(final int kiPower) {
        this.playerStats.remove(PlayerProperty.KI_POWER.toString());
        this.playerStats.addProperty(PlayerProperty.KI_POWER.toString(), kiPower);
    }

    public final void addPlayerKiPower(final int amt) {
        int current = this.getPlayerKiPower() + amt;
        this.setPlayerKiPower(current);
    }

    public final void setPlayerMaxStamina(final int maxStam) {
        this.talentPoints.remove(PlayerProperty.MAX_STAMINA.toString());
        this.talentPoints.addProperty(PlayerProperty.MAX_STAMINA.toString(), maxStam);
    }

    public final void addPlayerMaxStamina(final int amount) {
        int current = this.getPlayerMaxStamina() + amount;
        this.setPlayerMaxStamina(current);
    }

    public final void setPlayerStamina(final int amount) {
        this.playerStats.remove(PlayerProperty.CURRENT_STAMINA.toString());
        this.playerStats.addProperty(PlayerProperty.CURRENT_STAMINA.toString(), amount);
    }

    public final void addPlayerStamina(final int amount) {
        int current = this.getPlayerStamina() + amount;
        this.setPlayerStamina(current);
    }

    public final void setPlayerDefense(final int defense) {
        this.playerStats.remove(PlayerProperty.DEFENSE.toString());
        this.playerStats.addProperty(PlayerProperty.DEFENSE.toString(), defense);
    }

    public final void addPlayerDefense(final int amt) {
        int current = this.getPlayerDefense() + amt;
        this.setPlayerDefense(current);
    }

    public final void setPlayerRace(final Race race) {
        this.playerStats.remove(PlayerProperty.RACE.toString());
        this.playerStats.addProperty(PlayerProperty.RACE.toString(), race.toString());

        this.setConfigValues();
        if (this.getPlayerLevel() == 1) {
            this.addPlayerMaxHealth(this.defaultMaxHp);
            this.addPlayerDefense(this.defaultDefense);
            this.addPlayerMaxKi(this.defaultMaxKi);
            this.addPlayerMaxStamina(this.defaultMaxStam);
            this.addPlayerStrength(this.defaultStrength);
            this.addPlayerKiPower(this.defaultKiPow);
            return;
        }


        this.setConfigValues();
        this.reset(false);
        this.triggerUpdate();
    }

    public final void reset(final boolean killPlayer) {
        if (killPlayer) {
            final Player p = Bukkit.getPlayer(this.uuid);
            if (p != null) {
                p.setHealth(0D);
                this.setPlayerHealth(this.getPlayerMaxHealth());
                this.setPlayerKi(this.getPlayerMaxKi());
                this.setPlayerStamina(this.getPlayerMaxStamina());
            }
        }
    }

    public final int calculateDamage() {
        final int str = this.getPlayerStrength();
        final int tenPercent = getDoublePercentOf(str, 10);
        final int low = str - tenPercent;
        final int high = str + tenPercent;
        Logger.debug("Calculated Damage: Str: " + str + " low: " + low + " high: " + high);
        return this.plugin.getRandom().nextInt(high - low) + low;
    }

    public final void triggerUpdate() {
        Bukkit.getPluginManager().callEvent(new TriggerDataUpdate(this.uuid, this));
    }
}
