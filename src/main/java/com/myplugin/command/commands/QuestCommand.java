package com.myplugin.command.commands;

import com.myplugin.SpigotDragonZ;
import com.myplugin.command.DragonBallCommand;
import com.myplugin.lib.gui.guis.quest.QuestGui;
import com.myplugin.lib.json.config.enums.Race;
import com.myplugin.lib.json.data.player.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.myplugin.SpigotDragonZ.ofString;

public class QuestCommand extends DragonBallCommand {

    private final SpigotDragonZ plugin;

    public QuestCommand(final SpigotDragonZ plugin) {
        super("quest",
                "Accesses the Quests GUI",
                "/quest");

        this.plugin = plugin;
        this.plugin.registerCommand("quest", this);
    }

    @Override
    public void onCommand(final ConsoleCommandSender console, List<String> args) {
        console.sendMessage(Component.text(ofString("&cConsole cannot execute this command!")));
    }

    @Override
    public void onCommand(final Player player, List<String> args) {
        final PlayerData data = this.plugin.getDataManager().getPlayerData(player.getUniqueId());
        if (data.getRace() == Race.NONE) {
            player.sendMessage(Component.text(ofString("&cYou have no select a race! Select one with &e/race")));
            return;
        }

        player.openInventory(new QuestGui(data).inventory);
    }
}
