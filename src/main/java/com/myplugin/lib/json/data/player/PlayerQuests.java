package com.myplugin.lib.json.data.player;

import com.google.gson.JsonArray;

import java.util.ArrayList;

public class PlayerQuests {
    public boolean hasActiveMainQuest;
    public boolean hasActiveSideQuest;
    public int activeMainQuest;
    public int lastCompletedMainQuest;
    public ArrayList<Integer> activeSideQuests;
    public ArrayList<Integer> completedSideQuests;

    public PlayerQuests() {
        this.hasActiveMainQuest = false;
        this.activeMainQuest = -1;
        this.activeSideQuests = new ArrayList<>();
        this.lastCompletedMainQuest = -1;
        this.completedSideQuests = new ArrayList<>();
    }

    public final boolean hasActiveMainQuest() {
        return this.hasActiveMainQuest;
    }

    public final boolean hasActiveSideQuests() {
        return this.activeSideQuests.size() != 0;
    }
}
