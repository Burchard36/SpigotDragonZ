package com.myplugin.lib.json.data.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.json.config.JsonConfigManager;
import com.myplugin.lib.json.config.configs.HalfSaiyanConfig;
import com.myplugin.lib.json.config.configs.SaiyanConfig;
import com.myplugin.lib.json.config.configs.defaults.DefaultTalentPoints;
import com.myplugin.lib.json.config.configs.defaults.PerLevelIncrements;
import com.myplugin.lib.json.config.configs.defaults.PerTalentPoint;
import com.myplugin.lib.events.TriggerBossBarUpdate;
import com.myplugin.lib.events.TriggerConfigUpdate;
import com.myplugin.lib.events.TriggerDataUpdate;
import com.myplugin.lib.json.config.enums.PlayerProperty;
import com.myplugin.lib.json.config.enums.Race;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

import static com.myplugin.MyPlugin.*;

public class PlayerData implements Listener {

    /* Everything here is passed through JSON */
    public JsonObject currentData;
    public JsonObject talentPoints;

    private transient MyPlugin plugin;
    private transient UUID uuid;

    private transient PerTalentPoint perTalentPoint = null;
    private transient DefaultTalentPoints playerDefaults = null;
    private transient PerLevelIncrements perLvl = null;

    private transient PlayerTalentPoints playerTalentPoints;
    private transient PlayerCurrentData playerCurrentData;

    public PlayerData(final JsonObject currentData,
                      final JsonObject talentPoints,
                      final MyPlugin plugin,
                      final UUID uuid) {
        this.currentData = currentData;
        this.talentPoints = talentPoints;
        this.plugin = plugin;
        this.uuid = uuid;
        this.playerTalentPoints = this.plugin.getGson().fromJson(this.talentPoints, PlayerTalentPoints.class);
        this.playerCurrentData = this.plugin.getGson().fromJson(this.currentData, PlayerCurrentData.class);
        this.setConfigValues();
    }

    public void init(final MyPlugin plugin,
                     final UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.playerTalentPoints = this.plugin.getGson().fromJson(this.talentPoints, PlayerTalentPoints.class);
        this.playerCurrentData = this.plugin.getGson().fromJson(this.currentData, PlayerCurrentData.class);
        this.setConfigValues();
    }

    @EventHandler
    public void onConfigUpdate(final TriggerConfigUpdate e) {
        this.setConfigValues();
    }

    public void reloadJson() {
        this.currentData = this.plugin.getGson().toJsonTree(this.playerCurrentData, PlayerCurrentData.class).getAsJsonObject();
        this.talentPoints = this.plugin.getGson().toJsonTree(this.playerTalentPoints, PlayerTalentPoints.class).getAsJsonObject();
    }

    public void setConfigValues() {
        final JsonConfigManager manager = this.plugin.getConfigManager();
        this.reloadJson();

        switch(this.getRace()) {
            case SAIYAN: {
                final SaiyanConfig saiyanConfig = manager.getSaiyanConfig();
                this.perTalentPoint = saiyanConfig.getPerTalentPoint();
                this.perLvl = saiyanConfig.getPerLevelIncrements();
                this.playerDefaults = saiyanConfig.getDefaultTalentPoint();
                break;
            }

            case HALF_SAIYAN: {
                final HalfSaiyanConfig halfSaiyanConfig = manager.getHalfSaiyanConfig();
                this.perTalentPoint = halfSaiyanConfig.getPerTalentPoint();
                this.perLvl = halfSaiyanConfig.getPerLevelIncrements();
                this.playerDefaults = halfSaiyanConfig.getDefaultTalentPoint();
                break;
            }

            case NONE: {
                break;
            }
        }
    }

    /*
     * Everything below here is not passed to be serialized through google GSON
     * Think of these on a as instance basis
     */

    public final Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public final int getStrength() {
        if (this.getRace() == Race.NONE) return 0;
        final int strengthPointsSpent = this.playerTalentPoints.strengthTalentPoints + this.playerDefaults.defaultStrength;
        final int perTalentPoint = this.perTalentPoint.strength;
        final int perLevel = this.perLvl.strength;
        return  (strengthPointsSpent * perTalentPoint) + (this.getLevel() * perLevel);
    }

    public final int getMaxHealth() {
        if (this.getRace() == Race.NONE) return 0;
        final int healthPointsSpent = this.playerTalentPoints.healthPoints + this.playerDefaults.defaultMaxHealth;
        final int perTalentPoint = this.perTalentPoint.maxHealth;
        final int perLevel = this.perLvl.maxHealth;
        return (healthPointsSpent * perTalentPoint) + (this.getLevel() * perLevel);
    }

    public final int getCurrentHealth() {
        if (this.getRace() == Race.NONE) return 0;
        int health = this.playerCurrentData.currentHealth;
        if (health > this.getMaxHealth()) {
            health = this.getMaxHealth();
            this.setCurrentHealth(health);
        }
        return health;
    }

    public final int getCurrentKi() {
        if (this.getRace() == Race.NONE) return 0;
        int currentKi = this.playerCurrentData.currentKi;
        if (currentKi > this.getMaxKi()) {
            currentKi = this.getMaxKi();
            this.setCurrentKi(this.getMaxKi());
        }
        return currentKi;
    }

    public final int getMaxKi() {
        if (this.getRace() == Race.NONE) return 0;
        final int kiPointsSpent = this.playerTalentPoints.maxKiPoints + this.playerDefaults.defaultMaxKi;
        final int perTalentPoint = this.perTalentPoint.maxKi;
        final int perLevel = this.perLvl.maxKi;
        return (kiPointsSpent * perTalentPoint) + (this.getLevel() * perLevel);
    }

    public final int getDefense() {
        if (this.getRace() == Race.NONE) return 0;
        final int defensePointsSpent = this.playerTalentPoints.defensePoints + this.playerDefaults.defaultDefense;
        final int perTalentPoint = this.perTalentPoint.defense;
        final int perLevel = this.perLvl.defense;
        return (defensePointsSpent * perTalentPoint) + (this.getLevel() * perLevel);
    }

    public final int getKiPower() {
        if (this.getRace() == Race.NONE) return 0;
        final int kiPowerPointsSpent = this.playerTalentPoints.kiPowerPoints + this.playerDefaults.defaultKiPower;
        final int perTalentPoint = this.perTalentPoint.kiPower;
        final int perLevel = this.perLvl.kiPower;
        return (kiPowerPointsSpent * perTalentPoint) + (this.getLevel() * perLevel);
    }

    public final int getMaxStamina() {
        if (this.getRace() == Race.NONE) return 0;
        final int staminaPointsSpent = this.playerTalentPoints.maxStaminaPoints + this.playerDefaults.defaultMaxStamina;
        final int perTalentPoint = this.perTalentPoint.maxStamina;
        final int perLevel = this.perLvl.maxStamina;
        return (staminaPointsSpent * perTalentPoint) + (this.getLevel() * perLevel);
    }

    public final int getCurrentStamina() {
        if (this.getRace() == Race.NONE) return 0;
        int stamina = this.playerCurrentData.currentStamina;
        if (stamina > this.getMaxStamina()) stamina = this.getMaxStamina();
        return stamina;
    }

    public final Race getRace() {
        return this.playerCurrentData.getRace();
    }

    public final int getLevel() {
        if (this.getRace() == Race.NONE) return 0;
        return this.playerCurrentData.currentLevel;
    }

    public final int getCurrentExp() {
        if (this.getRace() == Race.NONE) return 0;
        return this.playerCurrentData.currentExp;
    }

    public final int getTalentPointsSpent() {
        if (this.getRace() == Race.NONE) return 0;
        return this.playerTalentPoints.spentTalentPoints;
    }

    public final int getTalentPoints() {
        if (this.getRace() == Race.NONE) return 0;
        final int trainingPointsSpent = this.playerTalentPoints.spentTalentPoints;
        int currentPoints = this.playerTalentPoints.currentTalentPoints;
        final int pointsPerLevel = this.perLvl.talentPoints;

        final int expectedPoints = this.getLevel() * pointsPerLevel;
        if (expectedPoints < (trainingPointsSpent + currentPoints)) {
            final int tpToAdd = expectedPoints - (trainingPointsSpent + currentPoints);
            this.playerTalentPoints.currentTalentPoints += tpToAdd;
            this.triggerUpdate();
            Bukkit.getPluginManager().callEvent(new TriggerDataUpdate(this.uuid, this));
        }
        return currentPoints;
    }

    public final int getMaxExp() {
        int maxExp = this.playerDefaults.maxExp;
        for (int x = 1; x <= this.getLevel(); x++) {
             maxExp += getPercentOf(maxExp, this.perLvl.percentExpIncrease);
        }
        return maxExp;
    }

    public final int removeHealth(final int amt) {
        int healthLeft = this.getCurrentHealth() - amt;
        if (healthLeft < 0) healthLeft = 0;
        this.playerCurrentData.currentHealth = healthLeft;
        return healthLeft;
    }

    public void applyDamage(final int incomingDamage) {
        int damageDealt = incomingDamage - (this.getDefense() / 2);
        int healthLeft = this.removeHealth(damageDealt);
        if (healthLeft <= 0) {
            this.reset(true);
        }
        Bukkit.getPluginManager().callEvent(new TriggerBossBarUpdate(this.uuid, this));
    }

    public final void setStrength(final int strength) {
        this.playerTalentPoints.strengthTalentPoints = strength;
    }

    public final void addStrength(final int amtToAdd) {
        int current = this.getStrength();
        current += amtToAdd;
        this.setStrength(current);
    }

    public final void setMaxHealth(final int health) {
        this.playerTalentPoints.healthPoints = health;
    }

    public final void addPlayerMaxHealth(final int amtToAdd) {
        int current = this.getMaxHealth() + amtToAdd;
        this.setMaxHealth(current);
    }

    public final void setCurrentHealth(final int health) {
        this.playerCurrentData.currentHealth = health;
    }

    public final void addPlayerHealth(final int health) {
        int current = this.getCurrentHealth() + health;
        this.setCurrentHealth(current);
    }

    public final void setMaxKi(final int maxKi) {
        this.playerTalentPoints.maxKiPoints = maxKi;
    }

    public final void addPlayerMaxKi(final int amount) {
        int current = this.getMaxKi() + amount;
        this.setMaxKi(current);
    }

    public final void setCurrentKi(final int currentKi) {
        this.playerCurrentData.currentKi = currentKi;
    }

    public final void addPlayerKi(final int amount) {
        int current = this.getCurrentKi() + amount;
        this.setCurrentKi(current);
    }

    public final void setKiPower(final int kiPower) {
        this.playerTalentPoints.kiPowerPoints = kiPower;
    }

    public final void addPlayerKiPower(final int amt) {
        int current = this.getKiPower() + amt;
        this.setKiPower(current);
    }

    public final void setMaxStamina(final int maxStam) {
        this.playerTalentPoints.maxStaminaPoints = maxStam;
    }

    public final void addMaxStamina(final int amount) {
        int current = this.getMaxStamina() + amount;
        this.setMaxStamina(current);
    }

    public final void setCurrentStamina(final int amount) {
        this.playerCurrentData.currentStamina = amount;
    }

    public final void addCurrentStamina(final int amount) {
        int current = this.getCurrentStamina() + amount;
        this.setCurrentStamina(current);
    }

    public final void setDefense(final int defense) {
        this.playerTalentPoints.defensePoints = defense;
    }

    public final void addDefense(final int amt) {
        int current = this.getDefense() + amt;
        this.setDefense(current);
    }

    public final void setCurrentExp(final int amt) {
        this.playerCurrentData.currentExp = amt;
    }

    public final void addCurrentExp(final int amt) {
        final int max = this.getMaxExp();
        final int current = this.getCurrentExp() + amt;
        if (current >= max) {
            this.addLevel();
            final int leftOver = max - current;
            if (leftOver > 0) {
                this.addCurrentExp(leftOver);
            } else this.setCurrentExp(leftOver);
        } else this.setCurrentExp(current);
    }

    public final void addLevel() {
        final int level = this.getLevel() + 1;
        this.playerCurrentData.currentLevel += 1;
        if (this.getPlayer() != null) {
            this.getPlayer().sendMessage(Component.text(ofString("&aSuccessfully leveled up to level: &b" + level)));
        }
    }

    public final void setRace(final Race race) {
        this.playerCurrentData.raceString = race.toString();
        this.setConfigValues();
        this.reset(false);
        this.triggerUpdate();
        this.triggerBarUpdate();;
    }

    public final void reset(final boolean killPlayer) {
        final Player p = Bukkit.getPlayer(this.uuid);
        this.setCurrentHealth(this.getMaxHealth());
        this.setCurrentKi(this.getMaxKi());
        this.setCurrentStamina(this.getMaxExp());
        if (killPlayer && p != null) {
            p.setHealth(0D);
        }
    }

    public final int calculateDamage() {
        final int str = this.getStrength();
        final int tenPercent = getDoublePercentOf(str, 10);
        final int low = str - tenPercent;
        final int high = str + tenPercent;
        Logger.debug("Calculated Damage: Str: " + str + " low: " + low + " high: " + high);
        return this.plugin.getRandom().nextInt(high - low) + low;
    }

    public final void triggerUpdate() {
        Bukkit.getPluginManager().callEvent(new TriggerDataUpdate(this.uuid, this));
    }

    public final void triggerBarUpdate() {
        Bukkit.getPluginManager().callEvent(new TriggerBossBarUpdate(this.uuid, this));
    }
}
