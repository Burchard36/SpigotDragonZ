package com.myplugin.lib.dragonball.gui.guis;

import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.PlayerData;
import com.myplugin.lib.dragonball.Race;
import com.myplugin.lib.dragonball.gui.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static com.myplugin.MyPlugin.ofString;

public class RaceGui extends Gui implements Listener {

    private final Race playerRace;
    private final MyPlugin plugin;

    public RaceGui(final PlayerData data, final MyPlugin plugin) {
        super(27, ofString("&b&lRace Gui Menu"), new RaceGuiHolder());
        this.plugin = plugin;
        this.playerRace = data.getPlayerRace();
        final ItemStack currentRaceItem = this.getCurrentRaceStack();
        this.inventory.setItem(13, currentRaceItem);
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

    public final ItemStack getCurrentRaceStack() {
        String itemName = "";
        Material material = Material.WHITE_DYE;
        switch (this.playerRace) {
            case SAIYAN: {
                itemName = "&3&lYour Race: &6&lSaiyan";
                material = Material.YELLOW_DYE;
                break;
            }
            case HALF_SAIYAN: {
                itemName = "&3&lYour Race: &e&lHalf&r&l-&6&lSaiyan";
                material = Material.ORANGE_DYE;
                break;
            }
            case NONE: {
                itemName = "&e&lYou have not selected a race! Select one below!";
                material = Material.WHITE_DYE;
                break;
            }
        }
        final ItemStack stack = new ItemStack(material, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString(itemName)));
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    @EventHandler
    public void onThisInventoryClick(final InventoryClickEvent e) {
        final Inventory inv = e.getInventory();
        final Player p = (Player) e.getWhoClicked();
        if (inv.getHolder() instanceof RaceGuiHolder) {
            e.setCancelled(true);
            final int clickedSlot = e.getSlot();
            if (clickedSlot == 13 && this.playerRace == Race.NONE) {
                p.closeInventory();
                p.openInventory(new RaceSelectionGui(this.plugin).inventory);
            }
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof RaceGuiHolder) {
            Logger.debug("Unregistered RaceGui because inventory closed.");
            HandlerList.unregisterAll(this);
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryOwnerLeave(final PlayerQuitEvent e) {
        Logger.debug("Unregistered RaceGui because player left.");
        HandlerList.unregisterAll(this);
    }

    public static final class RaceGuiHolder implements InventoryHolder {

        @Override
        public @NotNull Inventory getInventory() {
            /* WE WONT USE THIS OR SUPPORT THIS HAHA */
            return Bukkit.createInventory(this, 9);
        }
    }
}
