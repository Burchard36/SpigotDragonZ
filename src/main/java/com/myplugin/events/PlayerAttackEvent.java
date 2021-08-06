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

        final boolean playerHitsMob = e.getDamager() instanceof Player && !(e.getEntity() instanceof Player);
        final boolean playerHitsPlayer = e.getEntity() instanceof Player && e.getDamager() instanceof Player;
        final boolean mobHitsPlayer = !(e.getDamager() instanceof Player) && e.getEntity() instanceof Player;

        if (playerHitsMob) {
            final Player p = (Player) e.getDamager();
            final PlayerData data = this.manager.getPlayerData(p.getUniqueId());
            final int damage = data.calculateDamage();
            Logger.debug("Player with UUID: " + p.getUniqueId().toString() + " dealt " + damage + " to " + e.getEntity().getType().toString());
            e.setDamage(damage);
            this.spawnParticle(e.getEntity());

        } else if (playerHitsPlayer) {
            final Player hurtPlayer = (Player) e.getEntity();
            final Player attackingPlayer = (Player) e.getEntity();
            final PlayerData attacker = this.manager.getPlayerData(attackingPlayer.getUniqueId());
            final PlayerData defender = this.manager.getPlayerData(hurtPlayer.getUniqueId());
            final int attackerDamage = attacker.calculateDamage();
            defender.applyDamage(attackerDamage);
            e.setDamage(0D);
            this.spawnParticle(hurtPlayer);
            Logger.debug("Player with UUID: " + attackingPlayer.getUniqueId().toString() + " dealt " + attackerDamage + " to " + hurtPlayer.getUniqueId().toString());
        } else if (mobHitsPlayer) {
            final Player player = (Player) e.getEntity();
            final PlayerData data = this.manager.getPlayerData(player.getUniqueId());
            data.applyDamage((int)e.getDamage());
            this.spawnParticle(player);
            Logger.debug("Mob: " + e.getDamager().getType() + " dealt ~"+ e.getDamage()  + " to " + player.getUniqueId().toString());
            e.setDamage(0D);

        }
    }

    private void spawnParticle(final Entity e) {
        final World world = e.getWorld();
        final Location loc = e.getLocation();
        world.spawnParticle(Particle.CRIT_MAGIC, loc, 10);
    }
}
