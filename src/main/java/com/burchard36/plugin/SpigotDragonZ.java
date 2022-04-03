package com.burchard36.plugin;

import com.burchard36.Api;
import com.burchard36.Logger;
import com.burchard36.plugin.config.ConfigManager;
import com.burchard36.plugin.data.PlayerDataManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotDragonZ extends JavaPlugin implements Api {

    private PlayerDataManager dataManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        Logger.log("Initializing PlayerDataManager");
        Logger.init(this);
        this.configManager = new ConfigManager(this); // Load configs first
        this.dataManager = new PlayerDataManager(this); // afterwards load the data manager
    }

    @Override
    public void onDisable() {
        Logger.log("Saving player data. . .");
        this.dataManager.saveData();
        HandlerList.unregisterAll(this.dataManager);

        this.dataManager = null; // prevents leaks when reloading, GC should clear it but it's a failsafe since a instance is being stored in this class
    }

    /**
     * Gets the ConfigManage instance
     * @return instance of ConfigManager
     */
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public boolean isDebug() {
        return this.getConfig().getBoolean("DebugMode", false);
    }

    @Override
    public String loggerPrefix() {
        return "&b&lSpigotDragonZ";
    }
}
