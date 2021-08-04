package com.myplugin.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myplugin.lib.dragonball.Race;

public class PlayerData {

    /* Everything here is passed through JSON */
    public JsonObject playerStats;

    public PlayerData(final JsonObject playerStats) {
        this.playerStats = playerStats;
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
}
