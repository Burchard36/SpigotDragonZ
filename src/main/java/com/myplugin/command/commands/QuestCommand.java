package com.myplugin.command.commands;

import com.myplugin.MyPlugin;
import com.myplugin.command.DragonBallCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class QuestCommand extends DragonBallCommand {



    protected QuestCommand(final MyPlugin plugin) {
        super("quest",
                "Accesses the Quests GUI",
                "/quest");

        plugin.registerCommand("quest", this);
    }

    @Override
    public void onCommand(ConsoleCommandSender console, List<String> args) {

    }

    @Override
    public void onCommand(Player player, List<String> args) {

    }
}
