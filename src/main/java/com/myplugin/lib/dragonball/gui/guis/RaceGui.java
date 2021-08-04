package com.myplugin.lib.dragonball.gui.guis;

import com.myplugin.lib.PlayerData;
import com.myplugin.lib.dragonball.Race;
import com.myplugin.lib.dragonball.gui.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    public RaceGui(final PlayerData data) {
        super(27, ofString("Race Gui Menu"), new RaceGuiHolder());
        this.playerRace = data.getPlayerRace();
        final ItemStack currentRaceItem = this.getCurrentRaceStack();
        this.inventory.setItem(13, currentRaceItem);
    }

    public final Inventory getInventory() {
        return this.inventory;
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
    public void onThisInventoryClick(InventoryClickEvent e) {

    }

    @Override
    public void onThisInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof RaceGuiHolder) {
            HandlerList.unregisterAll(this);
        }
    }

    @Override
    public void onThisInventoryOwnerLeave(final PlayerQuitEvent e) {
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
