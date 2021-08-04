package com.myplugin.lib.events;

import com.myplugin.lib.PlayerData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TriggerDataUpdate extends Event implements Cancellable {

    private final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCanceled;
    private final UUID uuid;
    private final PlayerData data;

    public TriggerDataUpdate(final UUID uuid, final PlayerData data) {
        this.uuid = uuid;
        this.data = data;
        this.isCanceled = false;
    }

    public final UUID getUUID() {
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
}
