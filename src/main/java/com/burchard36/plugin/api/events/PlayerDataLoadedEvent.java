package com.burchard36.plugin.api.events;

import com.burchard36.json.JsonDataFile;
import com.burchard36.plugin.data.PlayerData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerDataLoadedEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean isCancelled = false;

    private final UUID loadedUuid;
    private final PlayerData dataFile;

    /**
     * Calls after a player data is loaded, cannot be cancelled (Why would you want to do this?)
     * @param uuidOfPlayer UUID of loaded player
     * @param file PlayerData file that is loaded
     */
    public PlayerDataLoadedEvent(final UUID uuidOfPlayer, final PlayerData file) {
        this.loadedUuid = uuidOfPlayer;
        this.dataFile = file;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public final UUID getLoadedUuid() {
        return this.loadedUuid;
    }

    public final PlayerData getPlayerData() {
        return this.dataFile;
    }
}
