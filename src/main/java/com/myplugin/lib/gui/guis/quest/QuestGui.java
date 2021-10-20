package com.myplugin.lib.gui.guis.quest;

import com.myplugin.SpigotDragonZ;
import com.myplugin.lib.gui.Gui;
import com.myplugin.lib.json.data.player.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import static com.myplugin.SpigotDragonZ.ofString;

public class QuestGui extends Gui implements Listener {

    private final PlayerData data;

    public QuestGui(final PlayerData data) {
        super(27, ofString("&a&lWhich Quests do you want to access?"), new QuestGuiHolder());
        this.data = data;

        final ItemStack mainQuestStack = this.getMainQuestStack();
        final ItemStack sideQuestStack = this.getSideQuestStack();

        this.inventory.setItem(12, mainQuestStack);
        this.inventory.setItem(14, sideQuestStack);
        this.fillEmptyWith(this.getBackgroundItem());

        Bukkit.getPluginManager().registerEvents(this, SpigotDragonZ.INSTANCE);
    }


    public final ItemStack getBackgroundItem() {
        final ItemStack stack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&f ")));
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getMainQuestStack() {
        final ItemStack stack = new ItemStack(Material.ENDER_EYE, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&e&l&oMain quest's")));
        final List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ofString("&f")));
        lore.add(Component.text(ofString("&e&oLeft-Click&7&o to access the main quest-line.")));
        meta.lore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getSideQuestStack() {
        final ItemStack stack = new ItemStack(Material.ENDER_PEARL, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString("&e&l&oSide quest's")));
        final List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ofString("&f")));
        lore.add(Component.text(ofString("&e&oLeft-Click&7&o to access your side quest's.")));
        meta.lore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    @EventHandler
    public void onThisInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof QuestGuiHolder) {
            e.setCancelled(true);

            if (e.getRawSlot() == 12) {
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().openInventory(new MainQuestGui(this.data).inventory);
            } else if (e.getRawSlot() == 14) {
                e.getWhoClicked().closeInventory();
            }
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryClose(final InventoryCloseEvent e) {
        if (e.getPlayer().getUniqueId() == this.data.getUuid() &&
            e.getInventory().getHolder() instanceof QuestGuiHolder) {
            HandlerList.unregisterAll(this);
        }
    }

    @Override
    @EventHandler
    public void onThisInventoryOwnerLeave(final PlayerQuitEvent e) {
        if (e.getPlayer().getUniqueId() == this.data.getUuid()) {
            HandlerList.unregisterAll(this);
        }
    }

    private static class QuestGuiHolder implements InventoryHolder {

        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(this, InventoryType.CHEST);
        }
    }
}
