package com.myplugin.lib.json.data.player;

import org.bukkit.configuration.file.FileConfiguration;

public class PlayerTalentPoints {

    public int strengthTalentPoints;
    public int healthPoints;
    public int defensePoints;
    public int maxKiPoints;
    public int kiPowerPoints;
    public int maxStaminaPoints;
    public int currentTalentPoints;
    public int spentTalentPoints;

    public PlayerTalentPoints() {
        this.strengthTalentPoints = 0;
        this.healthPoints = 0;
        this.defensePoints = 0;
        this.maxKiPoints = 0;
        this.maxStaminaPoints = 0;
        this.currentTalentPoints = 0;
        this.spentTalentPoints = 0;

        FileConfiguration conf;

        conf.getItemStack()
    }
}
