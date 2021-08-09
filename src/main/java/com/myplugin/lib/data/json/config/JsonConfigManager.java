package com.myplugin.lib.data.json.config;

import com.google.gson.Gson;
import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.data.json.config.configs.ServerSettings;
import com.myplugin.lib.data.json.config.configs.HalfSaiyanConfig;
import com.myplugin.lib.data.json.config.configs.SaiyanConfig;
import com.myplugin.lib.data.json.config.enums.ConfigType;
import com.myplugin.lib.events.TriggerConfigUpdate;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;

public class JsonConfigManager implements Listener {

    private final MyPlugin plugin;
    private final Gson gson;
    private HashMap<ConfigType, Object> serverConfigs;

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
        this.serverConfigs = new HashMap<>();

        final InputStream serverSettingsStream = this.plugin.getResource("serverConfig.json");
        final InputStream saiyanRaceConfigStream = this.plugin.getResource("saiyanRaceConfig.json");
        final InputStream halfSaiyanRaceConfigStream = this.plugin.getResource("halfSaiyanRaceConfig.json");

        final File serverConfigJson = new File(this.plugin.getDataFolder(), "/configs/serverConfig.json");
        final File saiyanRaceConfigJson = new File(this.plugin.getDataFolder(), "/configs/saiyanRaceConfig.json");
        final File halfSaiyanRaceConfigJson = new File(this.plugin.getDataFolder(), "/configs/halfSaiyanRaceConfig.json");

        try {
            if (!serverConfigJson.exists() && serverConfigJson.createNewFile()) {
                this.writeFile(ServerSettings.class, serverSettingsStream, serverConfigJson);
                Logger.log("Successfully created ServerSettings file.");
            }

            if (!saiyanRaceConfigJson.exists() && saiyanRaceConfigJson.createNewFile()) {
                this.writeFile(SaiyanConfig.class, saiyanRaceConfigStream, saiyanRaceConfigJson);
                Logger.log("Successfully created Saiyan config file.");
            }

            if (!halfSaiyanRaceConfigJson.exists() && halfSaiyanRaceConfigJson.createNewFile()) {
                this.writeFile(HalfSaiyanConfig.class, halfSaiyanRaceConfigStream, halfSaiyanRaceConfigJson);
                Logger.log("Successfully created Half-Saiyan config file.");
            }

            BufferedReader reader = Files.newBufferedReader(serverConfigJson.toPath());
            final ServerSettings serverSettings = this.gson.fromJson(reader, ServerSettings.class);
            this.serverConfigs.put(ConfigType.SERVER_SETTINGS, serverSettings);
            Logger.debug("Successfully loaded ServerSettings config file");

            reader = Files.newBufferedReader(saiyanRaceConfigJson.toPath());
            final SaiyanConfig saiyanConfig = this.gson.fromJson(reader, SaiyanConfig.class);
            this.serverConfigs.put(ConfigType.SAIYAN_CONFIG, saiyanConfig);
            Logger.debug("Successfully loaded Saiyan config file");

            reader= Files.newBufferedReader(halfSaiyanRaceConfigJson.toPath());
            final HalfSaiyanConfig halfSaiyanConfig = this.gson.fromJson(reader, HalfSaiyanConfig.class);
            this.serverConfigs.put(ConfigType.HALF_SAIYAN_CONFIG, halfSaiyanConfig);
            Logger.debug("Successfully loaded HalfSaiyan config file");

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

    public final ServerSettings getServerConfig() {
        return (ServerSettings) this.serverConfigs.get(ConfigType.SERVER_SETTINGS);
    }

    public final SaiyanConfig getSaiyanConfig() {
        return (SaiyanConfig) this.serverConfigs.get(ConfigType.SAIYAN_CONFIG);
    }

    public final HalfSaiyanConfig getHalfSaiyanConfig() {
        return (HalfSaiyanConfig) this.serverConfigs.get(ConfigType.HALF_SAIYAN_CONFIG);
    }
}
