package com.myplugin.lib.events;

import com.myplugin.lib.json.data.player.PlayerData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TriggerCacheUpdate extends Event implements Cancellable {

    public static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCanceled;

    private final UUID uuid;
    private final PlayerData data;

    public TriggerCacheUpdate(final UUID uuid,
                                final PlayerData data) {
        this.isCanceled = false;
        this.uuid = uuid;
        this.data = data;
    }

    public final UUID getUuid() {
        return this.uuid;
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
