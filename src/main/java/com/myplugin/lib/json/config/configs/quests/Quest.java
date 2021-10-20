package com.myplugin.lib.json.config.configs.quests;

import com.google.gson.JsonObject;
import com.myplugin.SpigotDragonZ;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.myplugin.SpigotDragonZ.ofString;

public class Quest {

    public int id;
    public int previous;
    public String name;
    public List<String> description;
    public String material;
    public JsonObject action;
    public JsonObject rewards;

    public QuestReward getRewards() {
        return SpigotDragonZ.INSTANCE.getGson().fromJson(this.rewards, QuestReward.class);
    }

    public QuestAction getAction() {
        return SpigotDragonZ.INSTANCE.getGson().fromJson(this.action, QuestAction.class);
    }

    public ItemStack getItemStack() {
        final ItemStack stack = new ItemStack(Material.valueOf(this.material), 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(ofString(this.name)));
        final List<Component> lore = new ArrayList<>();
        this.description.forEach((line) -> {
            lore.add(Component.text(ofString(line)));
        });
        meta.lore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
}
