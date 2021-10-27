package com.myplugin.lib.json.data.player;

import com.myplugin.lib.json.config.enums.Race;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerCurrentData {

    public int currentHealth;
    public int currentStamina;
    public int currentLevel;
    public int currentExp;
    public int currentKi;
    public String uuidString;
    public String raceString;

    public PlayerCurrentData() {
        this.currentHealth = 0;
        this.currentStamina = 0;
        this.currentLevel = 1;
        this.currentExp = 0;
        this.currentKi = 0;
        this.uuidString = "NONE";
        this.raceString = "NONE";
    }

    public void register() {
    }

    public final UUID getUuid() {
        if (this.uuidString.equalsIgnoreCase("NONE")) return null;
        return UUID.fromString(this.uuidString);
    }

    public final Race getRace() {
        return Race.valueOf(this.raceString);
    }
}
