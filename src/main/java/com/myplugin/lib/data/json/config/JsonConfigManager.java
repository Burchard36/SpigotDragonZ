package com.myplugin.lib.data.json.config;

import com.google.gson.Gson;
import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.data.json.config.configs.ServerSettings;
import com.myplugin.lib.data.json.config.configs.saiyan.SaiyanConfig;
import com.myplugin.lib.events.TriggerConfigUpdate;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.*;
import java.lang.reflect.Type;

public class JsonConfigManager implements Listener {

    private final MyPlugin plugin;
    private final Gson gson;

    public JsonConfigManager(final MyPlugin plugin) {
        this.plugin = plugin;
        this.gson = this.plugin.getGson();
        this.loadConfigs();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onReload(final TriggerConfigUpdate e) {
        this.loadConfigs();
    }

    private void loadConfigs() {
        this.validateDirs();


        final InputStream serverSettingsStream = this.plugin.getResource("serverConfig.json");
        final InputStream saiyanRaceConfigStream = this.plugin.getResource("saiyanRaceConfig.json");

        final File serverConfigJson = new File(this.plugin.getDataFolder(), "/configs/serverConfig.json");
        final File saiyanRaceConfigJson = new File(this.plugin.getDataFolder(), "/configs/saiyanRaceConfig.json");

        try {
            if (!serverConfigJson.exists() && serverConfigJson.createNewFile()) {
                this.writeFile(ServerSettings.class, serverSettingsStream, serverConfigJson);
            }

            if (!saiyanRaceConfigJson.exists() && saiyanRaceConfigJson.createNewFile()) {
                this.writeFile(SaiyanConfig.class, saiyanRaceConfigStream, saiyanRaceConfigJson);
            }
        } catch (final IOException ex) {
            Logger.error("Error creating config files");
            ex.printStackTrace();
        }
    }

    private void validateDirs() {
        final File file = new File(this.plugin.getDataFolder(), "/configs/");
        if (!file.exists()) {
            if (file.mkdirs()) {
                Logger.log("Successfully created /configs/ directory!");
            }
        }
    }

    private <T> void writeFile(T inst, final InputStream stream, final File file) {
        try {
            final FileWriter writer = new FileWriter(file);
            final Reader reader = new InputStreamReader(stream);
            final T config = this.gson.fromJson(reader, (Type) inst);
            this.gson.toJson(config, writer);
            writer.flush();
            writer.close();
        } catch (final IOException ex) {
            Logger.error("Error when writing config file");
            ex.printStackTrace();
        }
    }
}
