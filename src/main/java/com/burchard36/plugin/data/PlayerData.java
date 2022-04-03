package com.burchard36.plugin.data;

import com.burchard36.json.JsonDataFile;
import com.burchard36.plugin.config.ConfigManager;
import com.burchard36.plugin.data.types.RaceType;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class PlayerData extends JsonDataFile {

    @Getter @Setter private RaceType raceType;
    private final ConfigManager configManager;
    @Getter @Setter private double playerHealth;
    @Getter @Setter private double maxPlayerHealth;
    @Getter @Setter private int playerLevel;
    @Getter @Setter private int availableSkillPoints;
    @Getter @Setter private int strengthSkillPoints;
    @Getter @Setter private int defenseSkillPoints;
    @Getter @Setter private int healthSkillPoints;
    @Getter @Setter private int kiSkillPoints;
    @Getter @Setter private int kiPowerSkillPoints;

    public PlayerData(final File file,
                      final ConfigManager configManager) {
        super(file);
        this.configManager = configManager;
        this.raceType = RaceType.NONE;
        this.playerHealth = 20;
        this.maxPlayerHealth = 20;
        this.playerLevel = 1;
        this.availableSkillPoints = 0;
        this.strengthSkillPoints = 1;
        this.defenseSkillPoints = 1;
        this.healthSkillPoints = 1;
        this.kiSkillPoints = 1;
        this.kiPowerSkillPoints = 1;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // ADDERS
    ////////////////////////////////////////////////////////////////////////////////

    public final PlayerData addPlayerHealth(final double amt) {
        this.setPlayerHealth(this.getPlayerHealth() + amt);
        return this;
    }

    public final PlayerData addHealthPoints(final int amt) {
        this.setHealthSkillPoints(this.getHealthSkillPoints() + amt);
        return this;
    }

    public final PlayerData addDefensePoints(final int amt) {
        this.setDefenseSkillPoints(this.getDefenseSkillPoints() + amt);
        return this;
    }

    public final PlayerData addPlayerLevel(final int amt) {
        this.setPlayerLevel(this.getPlayerLevel() + amt);
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public final long getStrength() {
        return (long) this.configManager.getDefaultConfig().getInt("PlayerSettings.StrengthPerPoint", 5) * this.getStrengthSkillPoints();
    }


}
