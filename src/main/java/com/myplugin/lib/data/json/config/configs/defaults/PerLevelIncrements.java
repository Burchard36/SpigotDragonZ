package com.myplugin.lib.data.json.config.configs.defaults;

public class PerLevelIncrements {

    public int strength;
    public int kiPower;
    public int defense;
    public int maxKi;
    public int maxStamina;
    public int maxHealth;
    public int talentPoints;
    public int percentExpIncrease;

    public final int getDefense() {
        return this.defense;
    }

    public final int getKiPower() {
        return this.kiPower;
    }

    public final int getMaxHealth() {
        return this.maxHealth;
    }

    public final int getMaxKi() {
        return this.maxKi;
    }

    public final int getMaxStamina() {
        return this.maxStamina;
    }

    public final int getStrength() {
        return this.strength;
    }

    public final int getTalentPoints() {
        return this.talentPoints;
    }

    public final int getPercentExpIncrease() {
        return this.percentExpIncrease;
    }
}
