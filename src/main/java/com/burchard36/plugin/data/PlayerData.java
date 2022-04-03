package com.burchard36.plugin.data;

import com.burchard36.json.JsonDataFile;
import com.burchard36.plugin.config.ConfigManager;
import com.burchard36.plugin.data.types.RaceType;

import java.io.File;

public class PlayerData extends JsonDataFile {

    private final RaceType raceType;
    private final ConfigManager configManager;
    private double playerHealth;
    private double maxPlayerHealth;
    private int playerLevel;
    private int availableSkillPoints;
    private int strengthSkillPoints;
    private int defenseSkillPoints;
    private int healthSkillPoints;
    private int kiSkillPoints;
    private int kiPowerSkillPoints;

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

    /**
     * Gets the RaceType of this player
     * @return an enum of RaceType
     */
    public final RaceType getRaceType() {
        return this.raceType;
    }

    public final int getPlayerLevel() {
        return this.playerLevel;
    }

    public final int getAvailableSkillPoints() {
        return this.availableSkillPoints;
    }

    public final int getStrengthSkillPoints() {
        return this.strengthSkillPoints;
    }

    public final int getDefenseSkillPoints() {
        return this.defenseSkillPoints;
    }

    public final int getHealthSkillPoints() {
        return this.healthSkillPoints;
    }

    public final int getKiSkillPoints() {
        return this.kiSkillPoints;
    }

    public final int getKiPowerSkillPoints() {
        return this.kiPowerSkillPoints;
    }

    public final long getStrength() {
        return (long) this.configManager.getDefaultConfig().getInt("PlayerSettings.StrengthPerPoint", 5) * this.getStrengthSkillPoints();
    }

    public final long
}
