package com.myplugin.command.commands;

import com.myplugin.MyPlugin;
import com.myplugin.command.DragonBallCommand;
import com.myplugin.lib.gui.guis.stats.StatsGui;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.myplugin.MyPlugin.ofString;

public class TalentPointsCommand extends DragonBallCommand {

    private final MyPlugin plugin;

    public TalentPointsCommand(final MyPlugin plugin) {
        super("points",
                "Allows you to spend talent points.",
                "/points");
        this.plugin = plugin;
        plugin.registerCommand("points", this);
    }

    @Override
    public void onCommand(final ConsoleCommandSender console, List<String> args) {
        console.sendMessage(ofString("&cConsole cannot execute this command!"));
    }

    @Override
    public void onCommand(final Player player, List<String> args) {
        player.openInventory(
                new StatsGui(this.plugin.getDataManager().getPlayerData(player.getUniqueId())).inventory);
    }
}