package com.myplugin.lib.json.config.configs;

import com.google.gson.JsonObject;
import com.myplugin.lib.json.config.enums.ConfigType;

public class ServerSettings {
    public String key;
    public JsonObject cache;

    public ServerSettings() {}

    public final ConfigType getConfigType() {
        return ConfigType.valueOf(this.key);
    }

    public final int getCacheSaveTime() {
        return this.cache.get("saveIntervalInMinutes").getAsInt();
    }
}
