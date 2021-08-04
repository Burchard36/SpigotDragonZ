package com.myplugin.command.commands;

import com.myplugin.MyPlugin;
import com.myplugin.command.DragonBallCommand;
import com.myplugin.lib.PlayerData;
import com.myplugin.lib.PlayerDataManager;
import com.myplugin.lib.dragonball.Race;
import com.myplugin.lib.dragonball.gui.guis.RaceGui;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

import static com.myplugin.MyPlugin.ofString;

public class RaceCommand extends DragonBallCommand {

    private final PlayerDataManager manager;

    public RaceCommand(final MyPlugin plugin) {
        super("race", "View, create or change race", "/race");
        this.manager = plugin.getDataManager();
        plugin.registerCommand("race", this);
    }

    @Override
    public void onCommand(final ConsoleCommandSender console, final List<String> args) {
        console.sendMessage(ofString("&bSpigotDragonZ &4:: &cConsole cannot execute this command."));
    }

    @Override
    public void onCommand(final Player player, final List<String> args) {
        final PlayerData playerData = this.manager.getPlayerData(player.getUniqueId());
        final Inventory inventory = new RaceGui(playerData).getInventory();
        player.openInventory(inventory);
    }
}
