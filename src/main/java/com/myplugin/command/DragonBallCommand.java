package com.myplugin.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.myplugin.SpigotDragonZ.ofString;

public abstract class DragonBallCommand extends BukkitCommand {

    protected DragonBallCommand(final String command,
                                final String desc,
                                final String usage) {
        super(command);
        this.description = desc;
        this.usageMessage = ofString(usage);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        final List<String> newArgs = new ArrayList<>(Arrays.asList(args));
        if (sender instanceof Player) {
            this.onCommand((Player) sender, newArgs);
        } else this.onCommand((ConsoleCommandSender) sender, newArgs);
        return true;
    }

    /**
     * When a command is executes through console this is fired
     * @param console ConsoleCommandSender
     * @param args Args send by console
     */
    public abstract void onCommand(final ConsoleCommandSender console, final List<String> args);

    /**
     * When a command is executed through a player this is fired
     * @param player Player
     * @param args Args sent by Player
     */
    public abstract void onCommand(final Player player, final List<String> args);
}
