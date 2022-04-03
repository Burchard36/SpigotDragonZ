package com.burchard36.plugin.config;

import com.burchard36.plugin.SpigotDragonZ;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private final SpigotDragonZ plugin;
    private LocalFile languageFile;

    public ConfigManager(final SpigotDragonZ pluginInstance) {
        this.plugin = pluginInstance;
        this.loadConfigurations();
    }

    /**
     * Loads the configuration files and creates them if they do not already exist prior
     */
    private void loadConfigurations() {
        this.plugin.saveDefaultConfig();
        this.plugin.saveResource("EN_US.yml", false);

        final String languageFileString = this.plugin.getConfig().getString("Language", "EN_US").toUpperCase();

        switch (languageFileString) {
            case "EN_US":
             // TODO: Add more language file support
            default: {
                final File enUsFile = new File(this.plugin.getDataFolder(), "EN_US.yml");
                this.languageFile = new LocalFile(YamlConfiguration.loadConfiguration(enUsFile), this.plugin, enUsFile);
                break;
            }
        }
    }

    /**
     * Gets the language file thats currently active
     * @return LocalFile instance
     */
    public final LocalFile getLanguageFile() {
        return this.languageFile;
    }

    /**
     * Added this just so getting default config is easier for when we don't want to pass a plugin instance
     * @return FileConfiguration, it's the default spigot config.yml
     */
    public final FileConfiguration getDefaultConfig() {
        return this.plugin.getConfig();
    }
}
