package com.myplugin.lib.json.config.configs.quests;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class QuestReward {

    public int exp;
    public JsonArray commandRewards;

    public List<String> getCommandRewards() {
        final List<String> list = new ArrayList<>();
        this.commandRewards.forEach((ele) -> list.add(ele.getAsString()));
        return list;
    }
}
