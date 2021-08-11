package com.myplugin.lib.gui.guis.quest;

import com.myplugin.MyPlugin;
import com.myplugin.lib.gui.Gui;
import com.myplugin.lib.json.data.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import static com.myplugin.MyPlugin.ofString;

public class MainQuestGui extends Gui implements Listener {

    private final PlayerData data;

    public MainQuestGui(final PlayerData data) {
        super(27,
                ofString("&b&l&oMain Quests"),
                new MainQuestHolder());
        this.data = data;

        Bukkit.getPluginManager().registerEvents(this, MyPlugin.INSTANCE);
    }

    @Override
    @EventHandler
    public void onThisInventoryClick(final InventoryClickEvent e) {

    }

    @Override
    @EventHandler
    public void onThisInventoryClose(final InventoryCloseEvent e) {

    }

    @Override
    @EventHandler
    public void onThisInventoryOwnerLeave(final PlayerQuitEvent e) {
        if (e.getPlayer().getUniqueId() == this.data.getUuid()) {
            HandlerList.unregisterAll(this);
        }
    }

    private static class MainQuestHolder implements InventoryHolder {


        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(this, InventoryType.CHEST);
        }
    }
}
