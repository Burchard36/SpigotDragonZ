package com.myplugin.lib.gui.guis.stats;

import com.myplugin.lib.json.data.player.PlayerData;
import com.myplugin.lib.gui.Gui;
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
        final ItemStack strengthStack = this.getStrengthDisplayItem(this.data.getStrength());
        final ItemStack healthStack = this.getHealthDisplayItem(this.data.getCurrentHealth());
        final ItemStack defenseStack = this.getDefenseDisplayItem(this.data.getDefense());
    }

    private ItemStack getStrengthDisplayItem(final int strength) {
        final ItemStack stack = new ItemStack(Material.RED_DYE, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&c&lStrength")));
        final List<Component> comp = new ArrayList<>();
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&l&oCurrent strength points &6&l" + strength)));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&7&oStrength increases the damage power of your punches.")));
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
        comp.add(Component.text(ofString("&e&l&oCurrent defense points &6&l" + defense)));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&7&oDefense reduces incoming damage you take from\nPhysical and Ki attacks.")));
        meta.lore(comp);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getHealthDisplayItem(final int health) {
        final ItemStack stack = new ItemStack(Material.LIME_DYE , 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&a&lHealth")));
        final List<Component> comp = new ArrayList<>();
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&l&oCurrent health points &6&l" + health)));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&7&oYour health will decide how much total damage you take\nbefore you die,")));
        meta.lore(comp);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getStaminaDisplayItem(final int stamina) {
        final ItemStack stack = new ItemStack(Material.GREEN_DYE , 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&2&lStamina")));
        final List<Component> comp = new ArrayList<>();
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&l&oCurrent stamina points &6&l" + stamina)));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&7&o Your stamina will decide how much longer you can hold certain forms,\ndistance for running/flying and if it runs out your physical attacks\ndeal no damage.")));
        meta.lore(comp);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getMaxKiDisplayItem(final int maxKi) {
        final ItemStack stack = new ItemStack(Material.MAGENTA_DYE , 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&d&lMax Ki")));
        final List<Component> comp = new ArrayList<>();
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&l&oCurrent max ki points &6&l" + maxKi)));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&7&oMax Ki ultimately decides how long you hold a form for and\nhow many Ki attacks you can do.")));
        meta.lore(comp);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getKiPowerDisplayItem(final int kiPower) {
        final ItemStack stack = new ItemStack(Material.PURPLE_DYE , 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&5&lKi Power")));
        final List<Component> comp = new ArrayList<>();
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&l&oCurrent ki power points &6&l" + kiPower)));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&7&oKi Power ultimately decides how much damage your Ki\nattacks will inflict.")));
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
