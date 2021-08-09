package com.myplugin.lib.gui.guis.raceselection;

import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.data.json.PlayerDataManager;
import com.myplugin.lib.data.json.PlayerData;
import com.myplugin.lib.data.json.config.enums.Race;
import com.myplugin.lib.gui.Gui;
import com.myplugin.lib.events.TriggerBossBarUpdate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.myplugin.MyPlugin.ofString;

public class ConfirmRaceGui extends Gui implements Listener {

    private final Race race;
    private final MyPlugin plugin;
    private final UUID uuid;

    public ConfirmRaceGui(final MyPlugin plugin,
                          final Race selectedRace,
                          final UUID uuid) {
        super(27, ofString("&b&lConfirm race selection?"), new ConfirmRaceHolder());
        this.race = selectedRace;
        this.plugin = plugin;
        this.uuid = uuid;
        final ItemStack confirmStack = this.getConfirmStack();
        final ItemStack cancelStack =  this.getCancelStack();
        this.inventory.setItem(12, confirmStack);
        this.inventory.setItem(14, cancelStack);
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

    private ItemStack getConfirmStack() {
        final ItemStack stack = new ItemStack(Material.LIME_TERRACOTTA, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&a&lConfirm Selection")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getCancelStack() {
        final ItemStack stack = new ItemStack(Material.RED_TERRACOTTA, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&c&lCancel Selection")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    @EventHandler
    public void onThisInventoryClick(final InventoryClickEvent e) {
        final Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof ConfirmRaceHolder) {
            e.setCancelled(true);
            final Player p = (Player) e.getWhoClicked();
            final int slot = e.getSlot();
            if (slot == 12) {
                p.closeInventory();
                final PlayerDataManager manager = this.plugin.getDataManager();
                final PlayerData data = manager.getPlayerData(p.getUniqueId());
                data.setPlayerRace(this.race);
                data.triggerUpdate();
                this.plugin.getBarManager().loadPlayerBar(p.getUniqueId());
                Bukkit.getPluginManager().callEvent(new TriggerBossBarUpdate(p.getUniqueId(), data));
                p.sendMessage(Component.text(ofString("&aSuccessfully set you're race to &b" + this.race.toString().toLowerCase())));
            } else if (slot == 14) {
                p.closeInventory();
                p.openInventory(new RaceSelectionGui(this.plugin, this.uuid).inventory);
            }
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof ConfirmRaceHolder &&
            e.getPlayer().getUniqueId() == this.uuid) {
            Logger.debug("Unregistered ConfirmRaceGui because inventory closed.");
            HandlerList.unregisterAll(this);
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryOwnerLeave(final PlayerQuitEvent e) {
        if (e.getPlayer().getUniqueId() == this.uuid) {
            Logger.debug("Unregistered ConfirmRaceGui because player left.");
            HandlerList.unregisterAll(this);
        }
    }

    public static class ConfirmRaceHolder implements InventoryHolder {

        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(this, InventoryType.CHEST);
        }
    }
}
