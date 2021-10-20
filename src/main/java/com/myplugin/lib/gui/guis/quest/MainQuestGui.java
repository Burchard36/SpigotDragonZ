package com.myplugin.lib.gui.guis.quest;

import com.myplugin.SpigotDragonZ;
import com.myplugin.lib.Logger;
import com.myplugin.lib.gui.Gui;
import com.myplugin.lib.json.config.configs.QuestsConfig;
import com.myplugin.lib.json.config.configs.quests.Quest;
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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.myplugin.SpigotDragonZ.ofString;

public class MainQuestGui extends Gui implements Listener {

    private final PlayerData data;
    private final HashMap<Integer, HashMap<Integer, Quest>> quests;

    public MainQuestGui(final PlayerData data) {
        super(27,
                ofString("&b&l&oMain Quests"),
                new MainQuestHolder());
        this.data = data;
        this.quests = new HashMap<>();

        final QuestsConfig questsConfig = SpigotDragonZ.INSTANCE.getConfigManager().getQuestsConfig();
        final HashMap<Integer, List<Quest>> pageQuests = questsConfig.getMainQuestsByPage();
        final HashMap<Integer, Inventory> questInventories = new HashMap<>();

        pageQuests.keySet().forEach((page) -> {
            new ArrayList<>(pageQuests.values()).get(0).forEach((quest) -> {
                questInventories.computeIfAbsent(page, v -> Bukkit.createInventory(new MainQuestHolder(), 27));
                final Inventory inv = questInventories.get(page);
                inv.addItem(quest.getItemStack());
            });
        });

        this.inventory = questInventories.get(1);

        Bukkit.getPluginManager().registerEvents(this, SpigotDragonZ.INSTANCE);
    }

    private HashMap<Integer, ItemStack> getMainQuestStack() {
        return null;
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
