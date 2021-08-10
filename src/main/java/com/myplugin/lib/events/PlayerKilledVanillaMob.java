package com.myplugin.lib.events;

import com.myplugin.lib.json.data.player.PlayerData;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerKilledVanillaMob extends Event implements Cancellable {

    public static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCanceled;

    private final EntityType killed;
    private final PlayerData data;

    public PlayerKilledVanillaMob(final EntityType killed,
                                final PlayerData data) {
        this.isCanceled = false;
        this.killed = killed;
        this.data = data;
    }

    public final EntityType getKilled() {
        return this.killed;
    }

    public final PlayerData getData() {
        return this.data;
    }

    @Override
    public boolean isCancelled() {
        return this.isCanceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCanceled = true;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
