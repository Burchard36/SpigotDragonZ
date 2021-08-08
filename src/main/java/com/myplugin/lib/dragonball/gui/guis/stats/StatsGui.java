package com.myplugin.lib.dragonball.gui.guis.stats;

import com.myplugin.lib.PlayerData;
import com.myplugin.lib.dragonball.gui.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.myplugin.MyPlugin.ofString;

public class StatsGui extends Gui implements Listener {

    private final PlayerData data;
    private final UUID uuid;

    public StatsGui(final PlayerData data) {
        super(27, ofString("&b&lYou're stats"), new StatGuiHolder());

        this.data = data;
        this.uuid = this.data.getPlayer().getUniqueId();
        final ItemStack strengthStack = this.getStrengthDisplayItem(this.data.getPlayerStrength());
    }

    private ItemStack getStrengthDisplayItem(final int strength) {
        final ItemStack stack = new ItemStack(Material.RED_DYE, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&c&lStrength")));
        final List<Component> comp = new ArrayList<>();
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&l&oCurrent strength &6&l" + strength)));
        meta.lore(comp);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getDefenseDisplayItem(final int defense) {
        final ItemStack stack = new ItemStack(Material.LIGHT_BLUE_DYE, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&3&lDefense")));
        final List<Component> comp = new ArrayList<>();
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&l&oCurrent defense &6&l" + defense)));
        meta.lore(comp);
        stack.setItemMeta(meta);
        return stack;
    }

    public final ItemStack getBackgroundItem() {
        final ItemStack stack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&f ")));
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public void onThisInventoryClick(final InventoryClickEvent e) {
        final Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof StatGuiHolder) {

        }
    }

    @Override
    public void onThisInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof StatGuiHolder &&
            e.getPlayer().getUniqueId() == this.uuid) {
            HandlerList.unregisterAll(this);
        }
    }

    @Override
    public void onThisInventoryOwnerLeave(final PlayerQuitEvent e) {
        if (e.getPlayer().getUniqueId() == this.uuid) {
            HandlerList.unregisterAll(this);
        }
    }

    private static class StatGuiHolder implements InventoryHolder {

        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(this, InventoryType.CHEST);
        }
    }
}
