package com.myplugin.lib.dragonball.gui;


import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import static com.myplugin.MyPlugin.ofString;

public abstract class Gui {

    public final Inventory inventory;

    public Gui(final int slots, final String name, final InventoryHolder holder) {
        this.inventory = Bukkit.createInventory(holder, slots, Component.text(ofString(name)));
    }

    @EventHandler
    public abstract void onThisInventoryClick(final InventoryClickEvent e);
    @EventHandler
    public abstract void onThisInventoryClose(final InventoryCloseEvent e);
    @EventHandler
    public abstract void onThisInventoryOwnerLeave(final PlayerQuitEvent e);
}
