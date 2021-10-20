package com.myplugin.lib.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static com.myplugin.SpigotDragonZ.ofString;

public abstract class Gui {

    public Inventory inventory;

    public Gui(final int slots, final String name, final InventoryHolder holder) {
        this.inventory = Bukkit.createInventory(holder, slots, Component.text(ofString(name)));
    }

    public void fillEmptyWith(final ItemStack stack) {
        for (int x = 0; x <= this.inventory.getSize() - 1; x++) {
            if (this.inventory.getItem(x) == null) {
                this.inventory.setItem(x, stack);
            } else if (Objects.requireNonNull(this.inventory.getItem(x)).getType() == Material.AIR) {
                this.inventory.setItem(x, stack);
            }
        }
    }

    public abstract void onThisInventoryClick(final InventoryClickEvent e);
    public abstract void onThisInventoryClose(final InventoryCloseEvent e);
    public abstract void onThisInventoryOwnerLeave(final PlayerQuitEvent e);
}
