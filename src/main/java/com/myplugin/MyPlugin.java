package com.myplugin;

import com.myplugin.command.DragonBallCommand;
import com.myplugin.command.commands.RaceCommand;
import com.myplugin.events.PlayerTakesDamage;
import com.myplugin.lib.Logger;
import com.myplugin.lib.PlayerDataManager;
import com.myplugin.lib.config.ConfigPath;
import com.myplugin.lib.events.TriggerConfigUpdate;
import com.myplugin.lib.hud.BossBarManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class MyPlugin extends JavaPlugin implements Listener {

    private PlayerDataManager dataManager;
    private BossBarManager barManager;
    public static boolean DEBUG;


    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.setConfigValues();

        Logger.debug("Debug mode is enabled! You will receive a lot more message in your server console now, disable this if you want your outpost cleaner!");
        Logger.debug("Initializing PlayerDataManager. . .");
        this.dataManager = new PlayerDataManager(this);

        Logger.debug("Loading event BossBarManager");
        this.barManager = new BossBarManager(this);
        Logger.debug("Loading event PlayerDamageEvent");
        new PlayerTakesDamage(this);

        Logger.debug("Loading command RaceCommand");
        new RaceCommand(this);
    }

    @Override
    public void onDisable() {
        this.dataManager.closeRunnable();
        this.dataManager.saveAllCache(false);
    }

    @EventHandler
    public void onConfigUpdate(final TriggerConfigUpdate e) {
        this.setConfigValues();
    }

    private void setConfigValues() {
        DEBUG = this.getConfig().getBoolean(ConfigPath.DEBUG_MODE.toString());
    }

    public static String ofString(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static int getPercentOf(final int toGet, final int percentage) {
        return (int)((toGet * (percentage / 100.0f)) * 100);
    }

    public static double getDoublePercentOf(final int toGet, final int percentage) {
        return ((toGet * (percentage / 100.0f)));
    }

    /**
     * Registers a command to the Reflected CommandMap
     * @param commandName name of the command
     * @param command DragonBallCommand command to load
     */
    public void registerCommand(final String commandName, final DragonBallCommand command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = ((CommandMap) commandMapField.get(Bukkit.getServer()));
            commandMap.register(commandName, command);
        } catch (final IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the PlayerDataManager instance of this plugin
     * @return PlayerDataManager
     */
    public final PlayerDataManager getDataManager() {
        return this.dataManager;
    }

    /**
     * Gets the BossBarManager instance of this plugin
     * @return BossBarManager
     */
    public final BossBarManager getBarManager() {
        return this.barManager;
    }
}
