package com.myplugin.lib.gui.guis.raceselection;

import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.json.config.enums.Race;
import com.myplugin.lib.gui.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import java.util.UUID;

import static com.myplugin.MyPlugin.ofString;

public class RaceSelectionGui extends Gui implements Listener {

    private final MyPlugin plugin;
    private final UUID uuid;
    public RaceSelectionGui(final MyPlugin plugin, final UUID uuid) {
        super(27, ofString("&b&lRace Selection GUI"), new RaceSelectionHolder());
        this.plugin = plugin;
        this.uuid = uuid;
        final ItemStack saiyanStack = this.getSaiyanStack();
        final ItemStack halfSaiyanStack = this.getHalfSaiyanStack();
        this.inventory.setItem(11, saiyanStack);
        this.inventory.setItem(13, halfSaiyanStack);
        this.fillEmptyWith(this.getBackgroundItem());

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    public final ItemStack getBackgroundItem() {
        final ItemStack stack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&f ")));
        stack.setItemMeta(meta);
        return stack;
    }

    public final ItemStack getSaiyanStack() {
        final ItemStack stack = new ItemStack(Material.YELLOW_DYE, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&6&lSaiyan")));
        final ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(ofString("&f ")));
        lore.add(Component.text(ofString("&e&oLeft-Click&7 To choose Saiyan race!")));
        lore.add(Component.text(ofString("&f ")));
        lore.add(Component.text(ofString("&d&lRace Perks: ")));
        lore.add(Component.text(ofString("&a&l  + &bPhysical & Ki Strength")));
        lore.add(Component.text(ofString("&c&l  - &bStamina & health")));
        lore.add(Component.text(ofString("&a&l  + &bDefense")));
        meta.lore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public final ItemStack getHalfSaiyanStack() {
        final ItemStack stack = new ItemStack(Material.ORANGE_DYE, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&e&lHalf&r-&6&lSaiyan")));
        final ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(ofString("&f ")));
        lore.add(Component.text(ofString("&e&oLeft-Click&7 To choose Half-Saiyan race!")));
        lore.add(Component.text(ofString("&f ")));
        lore.add(Component.text(ofString("&d&lRace Perks: ")));
        lore.add(Component.text(ofString("&a&l  - &bPhysical & Ki Strength")));
        lore.add(Component.text(ofString("&c&l  + &bStamina & health")));
        lore.add(Component.text(ofString("&a&l  + &bDefense")));
        meta.lore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    @EventHandler
    public void onThisInventoryClick(final InventoryClickEvent e) {
        final Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof RaceSelectionHolder) {
            final Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            final int clicked = e.getSlot();
            if (clicked == 11) {
                p.openInventory(new ConfirmRaceGui(this.plugin, Race.SAIYAN, this.uuid).inventory);
            } else if (clicked == 13) {
                p.openInventory(new ConfirmRaceGui(this.plugin, Race.HALF_SAIYAN, this.uuid).inventory);
            }
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof RaceSelectionHolder &&
            e.getPlayer().getUniqueId() == this.uuid) {
            Logger.debug("Unregistered RaceSelectionGui because inventory closed.");
            HandlerList.unregisterAll(this);
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryOwnerLeave(final PlayerQuitEvent e) {
        if (e.getPlayer().getUniqueId() == this.uuid) {
            Logger.debug("Unregistered RaceSelectionGui because player left.");
            HandlerList.unregisterAll(this);
        }
    }

    public static class RaceSelectionHolder implements InventoryHolder {

        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(this, InventoryType.CHEST);
        }
    }
}
