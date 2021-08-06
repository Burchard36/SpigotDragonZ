package com.myplugin.events;

import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.PlayerData;
import com.myplugin.lib.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static com.myplugin.MyPlugin.getPercentOf;

public class PlayerAttackEvent implements Listener {

    private final PlayerDataManager manager;

    public PlayerAttackEvent(final MyPlugin plugin) {
        this.manager = plugin.getDataManager();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            final Player p = (Player) e.getDamager();
            final PlayerData data = this.manager.getPlayerData(p.getUniqueId());
            final int damage = data.calculateDamage();
            Logger.debug("Player with UUID: " + p.getUniqueId().toString() + " dealt " + damage + " to " + e.getEntity().getType().toString());
            e.setDamage(damage);

            final Location loc = e.getEntity().getLocation();
            final World world = loc.getWorld();
            world.spawnParticle(Particle.CRIT_MAGIC, loc, 10);
        }
    }
}
