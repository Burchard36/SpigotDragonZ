package com.burchard36.plugin.api.events;

import com.burchard36.json.JsonDataFile;
import com.burchard36.plugin.data.PlayerData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerDataLoadedEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean isCancelled = false;

    @Getter private final UUID playerUuid;
    @Getter private final PlayerData playerData;

    /**
     * Calls after a player data is loaded, cannot be cancelled (Why would you want to do this?)
     * @param uuidOfPlayer UUID of loaded player
     * @param file PlayerData file that is loaded
     */
    public PlayerDataLoadedEvent(final UUID uuidOfPlayer, final PlayerData file) {
        this.playerUuid = uuidOfPlayer;
        this.playerData = file;
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

    public final Player getPlayer() {
        return Bukkit.getPlayer(this.getPlayerUuid());
    }
}
