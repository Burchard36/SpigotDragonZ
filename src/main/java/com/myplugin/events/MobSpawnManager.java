package com.myplugin.events;

import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.events.TriggerConfigUpdate;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.HashMap;
import java.util.Set;

public class MobSpawnManager implements Listener {

    private HashMap<EntityType, MobStat> mobStats;
    private Set<EntityType> entitySet;
    private final MyPlugin plugin;

    public MobSpawnManager(final MyPlugin plugin) {
        this.plugin = plugin;
        this.setConfigValues();
        this.entitySet = this.mobStats.keySet();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onConfigUpdate(final TriggerConfigUpdate e) {
        this.setConfigValues();
        this.entitySet = this.mobStats.keySet();
    }

    @EventHandler
    public void onMobSpawn(final CreatureSpawnEvent e) {
        final EntityType type = e.getEntityType();
        if (this.entitySet.contains(type)) {
            final MobStat stat = this.mobStats.get(type);
            final AttributeInstance attackInstance =  e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            final AttributeInstance healthInstance =  e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attackInstance != null) {
                e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(stat.getMobAttack());
            }

            if (healthInstance != null) {
                e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(stat.getMobHealth());
                e.getEntity().setHealth(stat.getMobHealth());
            }
        }
    }

    private void setConfigValues() {
        this.mobStats = new HashMap<>();
        final FileConfiguration config = this.plugin.getConfig();
        final ConfigurationSection mobSec = config.getConfigurationSection("MobSettings");
        if (mobSec != null) {
            for (String key : mobSec.getKeys(false)) {
                final EntityType type = EntityType.valueOf(key);
                final ConfigurationSection data = mobSec.getConfigurationSection(key);
                if (data != null) {
                    final int health = data.getInt("Health");
                    final int attack = data.getInt("Attack");
                    final MobStat stat = new MobStat(health, attack);
                    this.mobStats.put(type, stat);
                    Logger.debug("Successfully loaded MobSettings for: " + type.toString());
                } else Logger.error("Config path MobSettings." + key+ " was null when got");
            }
        } else Logger.error("MobSetting value from config was null when got");

        Logger.log("Successfully loaded " + this.mobStats + " ModStats to override for vanilla spawns!s");
    }

    private static class MobStat {

        private final int mobHealth;
        private final int mobAttack;

        public MobStat(final int health, final int attack) {
            this.mobHealth = health;
            this.mobAttack = attack;
        }

        public final int getMobHealth() {
            return this.mobHealth;
        }

        public final int getMobAttack() {
            return this.mobAttack;
        }

    }
}
