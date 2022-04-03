package com.burchard36.plugin.config;

import com.burchard36.plugin.SpigotDragonZ;
import com.burchard36.plugin.api.exceptions.MaterialNotFoundException;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocalFile {

    private final YamlConfiguration config;
    private final SpigotDragonZ plugin;
    private final File file;

    public LocalFile(final YamlConfiguration configuration,
                     final SpigotDragonZ pluginInstance,
                     final File file) {
        this.config = configuration;
        this.plugin = pluginInstance;
        this.file = file;
    }

    /**
     * Saves the configuration file
     */
    private void save() {
        try {
            this.config.save(this.file);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets a config value, if one doesn't exist it will return the default value
     * @param path ConfigPath to use
     * @param defaultValue default string value
     * @return defaultValue variable if it doesn't exist, value from config if it doesn't
     */
    public final String getString(final ConfigPath path, String defaultValue) {
        return this.config.getString(path.get(), defaultValue);
    }

    /**
     * Sets a string to config and then sets it
     * @param path ConfigPath to set
     * @param valueToSet value to set
     */
    public final void setString(final ConfigPath path, String valueToSet) {
        this.config.set(path.get(), valueToSet);
        this.save();
    }

    public final Material getMaterial(final ConfigPath path, final Material def) {
        final String materialString = this.getString(path, def.toString()).toUpperCase();
        final Material resultMaterial = Material.getMaterial(materialString);
        if (resultMaterial == null) {
            new MaterialNotFoundException("Material could not be found in your language file! ConfigPath: " + path.get() + ". Material supplied by configuration: " + materialString).printStackTrace();
        }

        return resultMaterial;
    }

}
