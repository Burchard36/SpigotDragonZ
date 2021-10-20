package com.myplugin.events;

import com.myplugin.SpigotDragonZ;
import com.myplugin.lib.json.data.player.PlayerData;
import com.myplugin.lib.json.data.PlayerDataManager;
import com.myplugin.lib.events.TriggerConfigUpdate;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ExperienceHandler implements Listener {

    private final SpigotDragonZ plugin;
    private final PlayerDataManager manager;

    public ExperienceHandler(final SpigotDragonZ plugin) {
        this.plugin = plugin;
        this.manager = this.plugin.getDataManager();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onConfigReload(final TriggerConfigUpdate e) {
    }

    @EventHandler
    public void playerKill(final EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;

        e.getEntity().getKiller().setResourcePack("url", "");
        final LivingEntity entity = e.getEntity();
        final NamespacedKey key = new NamespacedKey(this.plugin, "exp");
        final PersistentDataContainer container = entity.getPersistentDataContainer();
        final Integer exp = container.get(key, PersistentDataType.INTEGER);

        if (exp == null || exp <= 0) return;
        if (e.getEntity().getKiller() == null) return;

        final PlayerData data = this.manager.getPlayerData(e.getEntity().getKiller().getUniqueId());
        data.addCurrentExp(exp);
        data.triggerUpdate();
    }
}
