package com.myplugin.lib.gui.guis.quest;

import com.myplugin.lib.gui.Gui;
import com.myplugin.lib.json.data.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import static com.myplugin.MyPlugin.ofString;

public class SideQuestGui extends Gui implements Listener {

    private final PlayerData data;

    public SideQuestGui(final PlayerData data) {
        super(27,
                ofString("&b&l&oSide Quests"),
                new SideQuestHolder());
        this.data = data;
    }

    @Override
    @EventHandler
    public void onThisInventoryClick(InventoryClickEvent e) {

    }

    @Override
    @EventHandler
    public void onThisInventoryClose(InventoryCloseEvent e) {

    }

    @Override
    @EventHandler
    public void onThisInventoryOwnerLeave(final PlayerQuitEvent e) {
        if (e.getPlayer().getUniqueId() == this.data.getUuid()) {
            HandlerList.unregisterAll(this);
        }
    }

    private static final class SideQuestHolder implements InventoryHolder {

        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(this, InventoryType.CHEST);
        }
    }
}
