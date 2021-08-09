package com.myplugin.events;

import com.myplugin.MyPlugin;
import com.myplugin.lib.Logger;
import com.myplugin.lib.data.json.PlayerData;
import com.myplugin.lib.data.json.PlayerDataManager;
import com.myplugin.lib.events.TriggerConfigUpdate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;

public class ExperienceHandler implements Listener {

    private final MyPlugin plugin;
    private final PlayerDataManager manager;
    private HashMap<EntityType, MobExp> vanillaMobs;

    public ExperienceHandler(final MyPlugin plugin) {
        this.plugin = plugin;
        this.manager = this.plugin.getDataManager();
        this.setConfigValues();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onConfigReload(final TriggerConfigUpdate e) {
        this.setConfigValues();
    }

    @EventHandler
    public void playerKill(final EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            final Player killer = e.getEntity().getKiller();
            final PlayerData data = this.manager.getPlayerData(killer.getUniqueId());
            final MobExp exp = this.vanillaMobs.get(e.getEntityType());
            if (exp.getMax() <= 0 && exp.getMin() <= 0) return;
            final int randExp = this.plugin.getRandom().nextInt(exp.getMax() - exp.getMin()) + exp.getMin();
            data.addExperience(randExp);
            Logger.debug("Player with UUID: " + killer.getUniqueId() + " receive " + randExp + " EXP for killing a " + e.getEntityType().toString());
        }
    }

    public void setConfigValues() {
        this.vanillaMobs = new HashMap<>();
        final FileConfiguration config = this.plugin.getConfig();
        final ConfigurationSection expSettings = config.getConfigurationSection("ExpSettings");
        if (expSettings == null) {
            Logger.error("Could not find field 'ExpSettings' inside of config.yml");
            return;
        }

        for (String s : expSettings.getKeys(false)) {
            final EntityType type = EntityType.valueOf(s);
            final ConfigurationSection sec = expSettings.getConfigurationSection(s);
            if (sec != null) {
                final int min = sec.getInt("ExpMin");
                final int max = sec.getInt("ExpMax");
                final MobExp exp = new MobExp(min, max);
                this.vanillaMobs.put(type, exp);
            } else Logger.error("Could not find config path ExpSettings." + s);
        }
    }

    private static final class MobExp {

        private final int min;
        private final int max;

        public MobExp(final int min, final int max) {
            this.min = min;
            this.max = max;
        }

        public final int getMin() {
            return this.min;
        }

        public final int getMax() {
            return this.max;
        }
    }
}
