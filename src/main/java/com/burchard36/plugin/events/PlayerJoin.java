package com.burchard36.plugin.events;

import com.burchard36.plugin.SpigotDragonZ;
import com.burchard36.plugin.api.events.PlayerDataLoadedEvent;
import com.burchard36.plugin.config.ConfigManager;
import com.burchard36.plugin.config.ConfigPath;
import com.burchard36.plugin.data.PlayerData;
import com.burchard36.plugin.data.types.RaceType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.burchard36.BurchAPI.convert;

public class PlayerJoin implements Listener {

    private final SpigotDragonZ plugin;
    private final ConfigManager configManager;

    public PlayerJoin(final SpigotDragonZ pluginInstance) {
        this.plugin = pluginInstance;
        this.configManager = this.plugin.getConfigManager();
    }

    /**
     * Use this event that way we 100% know when the player's data has loaded in
     * @param e PlayerDataLoadedEvent, provided by BukkitApi
     */
    @EventHandler
    public void onPlayerJoin(final PlayerDataLoadedEvent e) {
        final PlayerData data = e.getPlayerData();
        final Player player = e.getPlayer();

        if (data.getRaceType() == RaceType.NONE) { // If player hasnt selected a race, tell them here
            player.sendMessage(convert(this.configManager.getLanguageFile().getString(ConfigPath.path("NoRaceSelected"),
                    "&cYou have not selected a Race yet! You can do this by typing &e/race&c!")));
            return;
        }
    }
}
