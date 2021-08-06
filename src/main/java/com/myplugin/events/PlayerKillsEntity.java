package com.myplugin.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerKillsEntity implements Listener {



    public PlayerKillsEntity() {

    }

    @EventHandler
    public void onDeath(final EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            final Player killer = e.getEntity().getKiller();
            final EntityType killed = e.getEntityType();

        }
    }

}
