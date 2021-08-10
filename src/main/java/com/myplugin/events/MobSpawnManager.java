package com.myplugin.events;

import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.events.TriggerConfigUpdate;
import com.myplugin.lib.json.config.configs.MobsConfig;
import com.myplugin.lib.json.config.configs.mobs.CustomMob;
import com.myplugin.lib.json.config.configs.mobs.MobRadius;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

import static com.myplugin.MyPlugin.ofString;

public class MobSpawnManager implements Listener {

    private final MyPlugin plugin;
    private HashMap<Vector, List<String>> mobSpawns;
    private HashMap<String, CustomMob> customMobs;

    public MobSpawnManager(final MyPlugin plugin) {
        this.plugin = plugin;
        this.setConfigValues();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onConfigUpdate(final TriggerConfigUpdate e) {
        this.setConfigValues();
    }

    @EventHandler
    public void onMobSpawn(final CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.COMMAND) return;
        final double locX = e.getLocation().getX();
        final double locZ = e.getLocation().getZ();

        this.mobSpawns.keySet().forEach((key) -> {
            final int maxX = (int)key.getX();
            final int minX = ~(int)(key.getX() - 1);
            final int minZ = (int)key.getZ();
            final double maxZ = ~(int)(key.getZ() + 1);
            final boolean inMaxMin = locX <= maxX && locZ <= maxZ;
            final boolean inMinMax = locX <= minX && locZ <= minZ;
            if (inMaxMin || inMinMax) {
                this.mobSpawns.get(key).forEach((string) -> {
                    final CustomMob mob = this.customMobs.get(string);
                    if (EntityType.valueOf(mob.type) == e.getEntity().getType()) {

                        this.processEntity(e.getEntity(), mob);

                        if (mob.spawnAmount > 1) {
                            for (int x = 0; mob.spawnAmount > x; x++) {
                                e.getEntity().getWorld().spawnEntity(e.getLocation(), e.getEntityType(),
                                        CreatureSpawnEvent.SpawnReason.COMMAND, (entity) ->
                                                this.processEntity((LivingEntity) entity, mob));
                            }
                        }
                    }
                });
            }
        });
    }

    ;
    public void processEntity(final LivingEntity e, final CustomMob mob) {
        if (mob.attack != -1) {
            e.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mob.attack);
        }

        if (mob.health != -1) {
            e.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mob.health);
            e.setHealth(mob.health);
        }

        if (mob.exp != -1) {
            final NamespacedKey namespacedKey = new NamespacedKey(MyPlugin.INSTANCE, "exp");
            e.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, mob.exp);
        }
    }

    private void setConfigValues() {
        this.mobSpawns = new HashMap<>();
        this.customMobs = new HashMap<>();
        final MobsConfig config = this.plugin.getConfigManager().getMobsConfig();
        final List<MobRadius> mobRadius = config.getMobRadius();
        final List<CustomMob> mobs = config.getCustomMobs();

        mobs.forEach((customMob) -> this.customMobs.put(customMob.key, customMob));
        mobRadius.forEach((radius) -> {
            final Vector vec = new Vector(radius.x, 0, radius.z);
            final ArrayList<String> toSpawn = new ArrayList<>();
            radius.mobs.forEach((mob) -> {
                final String key = mob.getAsString();
                mobs.forEach((customMob) -> {
                    if (customMob.key.equalsIgnoreCase(key)) {
                        toSpawn.add(key);
                    } else Logger.error("Trying to load mob with invalid KEY: " + key);
                });
            });
            Logger.log("Loaded MobSpawn radius: " + vec.getX() + ":" + vec.getZ() + ", Loaded: " + toSpawn.size() + " mobs for this region.");
            this.mobSpawns.put(vec, toSpawn);
        });
    }
}
