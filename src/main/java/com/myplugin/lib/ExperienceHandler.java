package com.myplugin.lib;

import com.myplugin.MyPlugin;
import com.myplugin.lib.events.TriggerConfigUpdate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class ExperienceHandler implements Listener {

    private final MyPlugin plugin;
    private HashMap<EntityType, Integer> vanillaMobs;

    public ExperienceHandler(final MyPlugin plugin) {
        this.plugin = plugin;
        this.vanillaMobs = new HashMap<>();
    }

    @EventHandler
    public void onConfigReload(final TriggerConfigUpdate e) {

    }

    public void setConfigValues() {
        final FileConfiguration config = this.plugin.getConfig();
        final ConfigurationSection expSettings = config.getConfigurationSection("ExpSettings");
        if (expSettings == null) {
            Logger.error("Could not find field 'ExpSettings' inside of config.yml");
            return;
        }

        for (String s : expSettings.getKeys(false)) {
            final EntityType type = EntityType.valueOf(s);
            final ConfigurationSection sec = expSettings.getConfigurationSection(s);

        }
    }
}
