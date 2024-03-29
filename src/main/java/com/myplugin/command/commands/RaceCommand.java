package com.myplugin.command.commands;

import com.myplugin.SpigotDragonZ;
import com.myplugin.command.DragonBallCommand;
import com.myplugin.lib.json.data.player.PlayerData;
import com.myplugin.lib.json.data.PlayerDataManager;
import com.myplugin.lib.gui.guis.raceselection.RaceGui;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

import static com.myplugin.SpigotDragonZ.ofString;

public class RaceCommand extends DragonBallCommand {

    private final PlayerDataManager manager;
    private final SpigotDragonZ plugin;

    public RaceCommand(final SpigotDragonZ plugin) {
        super("race", "View, create or change race", "/race");
        this.plugin = plugin;
        this.manager = this.plugin.getDataManager();
        plugin.registerCommand("race", this);
    }

    @Override
    public void onCommand(final ConsoleCommandSender console, final List<String> args) {
        console.sendMessage(ofString("&bSpigotDragonZ &4:: &cConsole cannot execute this command."));
    }

    @Override
    public void onCommand(final Player player, final List<String> args) {
        final PlayerData playerData = this.manager.getPlayerData(player.getUniqueId());
        final Inventory inventory = new RaceGui(playerData, this.plugin).inventory;
        player.openInventory(inventory);
    }
}
