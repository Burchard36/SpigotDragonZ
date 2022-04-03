package com.burchard36.plugin.guis;

import com.burchard36.inventory.ClickableItem;
import com.burchard36.inventory.PluginInventory;
import com.burchard36.plugin.config.ConfigPath;
import com.burchard36.plugin.config.LocalFile;
import com.burchard36.plugin.data.PlayerData;
import org.bukkit.Material;

import static com.burchard36.BurchAPI.convert;

public class RaceSelectionGui {

    private final PlayerData playerData;
    private final PluginInventory inventory;
    private final LocalFile localFile;

    public RaceSelectionGui(final PlayerData data,
                            final LocalFile file) {
        this.playerData = data;
        this.localFile = file;
        this.inventory = new PluginInventory(27,
                convert(this.localFile.getString(ConfigPath.path("Guis.RaceSelection.InventoryTitle"), "&b&lSelect a race!")));

        final ClickableItem backgroundItem = new ClickableItem(this.localFile.getMaterial(ConfigPath.path("Guis.RaceSelection.BackgroundItem"), Material.BLACK_STAINED_GLASS), 1);
        final ClickableItem humanItem = new ClickableItem()
    }


}
