package com.myplugin.lib.json.config.configs;

import com.google.gson.JsonObject;
import com.myplugin.MyPlugin;
import com.myplugin.lib.json.config.configs.defaults.DefaultTalentPoints;
import com.myplugin.lib.json.config.configs.defaults.PerLevelIncrements;
import com.myplugin.lib.json.config.configs.defaults.PerTalentPoint;
import com.myplugin.lib.json.config.enums.ConfigType;

public class HalfSaiyanConfig {

    public String key;
    public JsonObject defaultTalentPoints;
    public JsonObject perLevelPointIncrements;
    public JsonObject perTalentPoint;

    public final ConfigType getConfigType() {
        return ConfigType.valueOf(this.key);
    }

    public final DefaultTalentPoints getDefaultTalentPoint() {
        return MyPlugin.INSTANCE.getGson().fromJson(this.defaultTalentPoints, DefaultTalentPoints.class);
    }

    public final PerLevelIncrements getPerLevelIncrements() {
        return MyPlugin.INSTANCE.getGson().fromJson(this.perLevelPointIncrements, PerLevelIncrements.class);
    }

    public final PerTalentPoint getPerTalentPoint() {
        return MyPlugin.INSTANCE.getGson().fromJson(this.perTalentPoint, PerTalentPoint.class);
    }

}
