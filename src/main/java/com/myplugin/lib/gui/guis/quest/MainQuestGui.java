package com.myplugin.lib.gui.guis.quest;

import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.gui.Gui;
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

import java.util.HashMap;

import static com.myplugin.MyPlugin.ofString;

public class MainQuestGui extends Gui implements Listener {

    private final PlayerData data;
    private final HashMap<Integer, HashMap<Integer, Quest>> quests;

    public MainQuestGui(final PlayerData data) {
        super(27,
                ofString("&b&l&oMain Quests"),
                new MainQuestHolder());
        this.data = data;
        this.quests = new HashMap<>();

        final HashMap<Integer, Quest> questMap = MyPlugin.INSTANCE.getConfigManager().getQuestsConfig().getMainQuests();
        final int questSize = questMap.size();

        int currentSlot = 9;
        int currentPage = 0;
        int i;
        HashMap<Integer, Quest> pageQuests = new HashMap<>();
        for (i = 0; i <= (questSize - 1); i++) {
            if (currentSlot > 17) {
                Logger.debug("Loaded quest page: " + currentPage + " with " + pageQuests.size() + " quests.");
                this.quests.put(currentPage, pageQuests);
                currentSlot = 9;
                currentPage++;
            }
            if (currentSlot == 9) {
                pageQuests = new HashMap<>();
            }

            if (questMap.get(i) != null) {
                Logger.debug("Loaded quest on page: " + currentPage + " on slot: " + currentSlot);
                pageQuests.put(currentSlot, questMap.get(i));
            }
            currentSlot++;
        }

        if (currentSlot <= 16 && !pageQuests.isEmpty()) {
            Logger.debug("Loaded leftover quest page: " + currentPage + " with " + pageQuests.size() + " quests.");
            this.quests.put(currentPage, pageQuests);
        }

        Bukkit.getPluginManager().registerEvents(this, MyPlugin.INSTANCE);
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
