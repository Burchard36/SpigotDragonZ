package com.myplugin.lib.json.config.configs;

import com.google.gson.JsonArray;
import com.myplugin.MyPlugin;
import com.myplugin.lib.json.config.configs.mobs.CustomMob;
import com.myplugin.lib.json.config.configs.mobs.MobRadius;
import org.bukkit.entity.Mob;

import java.util.ArrayList;
import java.util.List;

public class MobsConfig {

    public JsonArray radius;
    public JsonArray mobs;

    public final List<MobRadius> getMobRadius() {
        final List<MobRadius> list = new ArrayList<>();
        this.radius.forEach((element) -> {
            final MobRadius radius = MyPlugin.INSTANCE.getGson().fromJson(element.getAsJsonObject(), MobRadius.class);
            list.add(radius);
        });
        return list;
    }

    public final List<CustomMob> getCustomMobs() {
        final List<CustomMob> list = new ArrayList<>();
        this.mobs.forEach((element) -> {
            final CustomMob mob = MyPlugin.INSTANCE.getGson().fromJson(element.getAsJsonObject(), CustomMob.class);
            list.add(mob);
        });
        return list;
    }
}
