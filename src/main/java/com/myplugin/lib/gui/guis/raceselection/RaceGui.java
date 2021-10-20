package com.myplugin.lib.gui.guis.raceselection;

import com.myplugin.SpigotDragonZ;
import com.myplugin.lib.Logger;
import com.myplugin.lib.json.data.player.PlayerData;
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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.myplugin.SpigotDragonZ.ofString;

public class RaceGui extends Gui implements Listener {

    private final Race playerRace;
    private final SpigotDragonZ plugin;
    private final PlayerData data;

    public RaceGui(final PlayerData data, final SpigotDragonZ plugin) {
        super(27, ofString("&b&lRace Gui Menu"), new RaceGuiHolder());
        this.plugin = plugin;
        this.data = data;
        this.playerRace = this.data.getRace();
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
        final List<Component> lore = new ArrayList<>();
        Material material = Material.WHITE_DYE;
        switch (this.playerRace) {
            case SAIYAN: {
                itemName = "&3&lYour Race: &6&lSaiyan";
                material = Material.YELLOW_DYE;
                lore.add(Component.text(ofString("&f ")));
                break;
            }
            case HALF_SAIYAN: {
                itemName = "&3&lYour Race: &e&lHalf&r&l-&6&lSaiyan";
                material = Material.ORANGE_DYE;
                lore.add(Component.text(ofString("&f ")));
                break;
            }
            case NONE: {
                itemName = "&e&lYou have not selected a race! Select one below!";
                material = Material.WHITE_DYE;
                lore.add(Component.text(ofString("&f ")));
                lore.add(Component.text(ofString("&e&oLeft-Click&7 To select your race!")));
                break;
            }
        }
        final ItemStack stack = new ItemStack(material, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString(itemName)));
        meta.lore(lore);
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
                p.openInventory(new RaceSelectionGui(this.plugin, this.data.getPlayer().getUniqueId()).inventory);
            }

            HandlerList.unregisterAll(this);
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof RaceGuiHolder &&
            e.getPlayer().getUniqueId() == this.data.getPlayer().getUniqueId()) {
            Logger.debug("Unregistered RaceGui because inventory closed.");
            HandlerList.unregisterAll(this);
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryOwnerLeave(final PlayerQuitEvent e) {
        if (e.getPlayer().getUniqueId() == this.data.getPlayer().getUniqueId()) {
            Logger.debug("Unregistered RaceGui because player left.");
            HandlerList.unregisterAll(this);
        }
    }

    public static final class RaceGuiHolder implements InventoryHolder {

        @Override
        public @NotNull Inventory getInventory() {
            /* WE WONT USE THIS OR SUPPORT THIS HAHA */
            return Bukkit.createInventory(this, 9);
        }
    }
}
