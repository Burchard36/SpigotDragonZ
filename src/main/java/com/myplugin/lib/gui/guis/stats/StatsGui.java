package com.myplugin.lib.gui.guis.stats;

import com.myplugin.MyPlugin;
import com.myplugin.lib.json.data.player.PlayerData;
import com.myplugin.lib.gui.Gui;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
        this.rebuildInventory();
        this.fillEmptyWith(this.getBackgroundItem());

        Bukkit.getPluginManager().registerEvents(this, MyPlugin.INSTANCE);
    }

    private void rebuildInventory() {
        final ItemStack talentPointsStack = this.getCurrentTalentPointsDisplayItem(this.data.playerTalentPoints.currentTalentPoints);
        final ItemStack strengthStack = this.getStrengthDisplayItem(this.data.playerTalentPoints.strengthTalentPoints);
        final ItemStack healthStack = this.getHealthDisplayItem(this.data.playerTalentPoints.healthPoints);
        final ItemStack defenseStack = this.getDefenseDisplayItem(this.data.playerTalentPoints.defensePoints);
        final ItemStack maxKiStack = this.getMaxKiDisplayItem(this.data.playerTalentPoints.maxKiPoints);
        final ItemStack kiPowerStack = this.getKiPowerDisplayItem(this.data.playerTalentPoints.kiPowerPoints);
        final ItemStack maxStaminaStack = this.getStaminaDisplayItem(this.data.playerTalentPoints.maxStaminaPoints);

        this.inventory.setItem(11, strengthStack);
        this.inventory.setItem(12, healthStack);
        this.inventory.setItem(13, talentPointsStack);
        this.inventory.setItem(4, defenseStack);
        this.inventory.setItem(22, maxStaminaStack);
        this.inventory.setItem(14, maxKiStack);
        this.inventory.setItem(15, kiPowerStack);
    }

    private ItemStack getCurrentTalentPointsDisplayItem(final int points) {
        final ItemStack stack = new ItemStack(Material.SUNFLOWER, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&e&l&oCurrent Talent Points&6&l&o " + points)));
        stack.setItemMeta(meta);
        return stack;
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
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&oLeft-Click&7&o To spend a Talent Points on this skill.")));
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
        comp.add(Component.text(ofString("&7&oDefense reduces incoming damage you take from")));
        comp.add(Component.text(ofString("&7&oPhysical and Ki attacks.")));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&oLeft-Click&7&o To spend a Talent Points on this skill.")));
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
        comp.add(Component.text(ofString("&7&oYour health will decide how much total damage you ")));
        comp.add(Component.text(ofString("&7&otakebefore you die,")));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&oLeft-Click&7&o To spend a Talent Points on this skill.")));
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
        comp.add(Component.text(ofString("&7&oYour stamina will decide how much longer you can hold")));
        comp.add(Component.text(ofString("&7&ocertain forms, distance for running/flying and if it runs out")));
        comp.add(Component.text(ofString("&7&oyour physical attacks will no longer deal no damage.")));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&oLeft-Click&7&o To spend a Talent Points on this skill.")));
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
        comp.add(Component.text(ofString("&7&oMax Ki ultimately decides how long you hold a form for and")));
        comp.add(Component.text(ofString("&7&ohow many Ki attacks you can do.")));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&oLeft-Click&7&o To spend a Talent Points on this skill.")));
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
        comp.add(Component.text(ofString("&7&oKi Power ultimately decides how much damage your Ki")));
        comp.add(Component.text(ofString("&7&oattacks will inflict.")));
        comp.add(Component.text(ofString("&f ")));
        comp.add(Component.text(ofString("&e&oLeft-Click&7&o To spend a Talent Points on this skill.")));
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
    @EventHandler
    public void onThisInventoryClick(final InventoryClickEvent e) {
        final Inventory inv = e.getInventory();
        final Player p = (Player) e.getWhoClicked();
        if (inv.getHolder() instanceof StatGuiHolder) {
            e.setCancelled(true);

            if (e.getRawSlot() == 13) return;
            if (e.getCurrentItem() == this.getBackgroundItem()) return;

            if (data.playerTalentPoints.currentTalentPoints <= 0) {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                p.sendMessage(Component.text(ofString("&c&oYou do not have enough Talent Points for this!")));
                return;
            }

            switch (e.getRawSlot()) {
                case 11: { // Strength
                    this.data.addStrength(1);
                    this.data.spendTalentPoint();
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    p.sendMessage(Component
                            .text(ofString("&e&oSuccessfully added &6&o1&e&o point into strength")));
                    break;
                }

                case 12: { // Max Health
                    this.data.addMaxHealth(1);
                    this.data.spendTalentPoint();
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    p.sendMessage(Component
                            .text(ofString("&e&oSuccessfully added &6&o1&e&o point into max health")));
                    break;
                }

                case 4: { // Defense
                    this.data.addDefense(1);
                    this.data.spendTalentPoint();
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    p.sendMessage(Component
                            .text(ofString("&e&oSuccessfully added &6&o1&e&o point into defence")));
                    break;
                }

                case 22: { // Max Stamina
                    this.data.addMaxStamina(1);
                    this.data.spendTalentPoint();
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    p.sendMessage(Component
                            .text(ofString("&e&oSuccessfully added &6&o1&e&o point into max stamina")));
                    break;
                }

                case 14: { // Max Ki
                    this.data.addMaxKi(1);
                    this.data.spendTalentPoint();
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    p.sendMessage(Component
                            .text(ofString("&e&oSuccessfully added &6&o1&e&o point into max ki")));
                    break;
                }

                case 15: {
                    this.data.addKiPower(1);
                    this.data.spendTalentPoint();
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    p.sendMessage(Component
                            .text(ofString("&e&oSuccessfully added &6&o1&e&o point into ki power")));
                    break;
                }
            }

            this.rebuildInventory();
            this.data.getPlayer().updateInventory();
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof StatGuiHolder &&
            e.getPlayer().getUniqueId() == this.uuid) {
            HandlerList.unregisterAll(this);
        }
    }

    @Override
    @EventHandler
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
