package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;

import java.io.File;

public class DefaultGenerator {
    GiftBox plugin;

    public DefaultGenerator(GiftBox plugin){
        this.plugin = plugin;
    }

    public void check(){
        File folder = plugin.getDataFolder();
        if(folder.exists()) return;

        plugin.getLogger().info("generating default config");

        plugin.saveResource("boxes/epic.yml", false);
        plugin.saveResource("boxes/normal.yml", false);
        plugin.saveResource("boxes/rare.yml", false);

        plugin.saveResource("items/epic.yml", false);
        plugin.saveResource("items/normal.yml", false);
        plugin.saveResource("items/rare.yml", false);

        plugin.saveResource("skins/epic/body.png", false);
        plugin.saveResource("skins/epic/lid.png", false);

        plugin.saveResource("skins/normal/body.png", false);
        plugin.saveResource("skins/normal/lid.png", false);

        plugin.saveResource("skins/rare/body.png", false);
        plugin.saveResource("skins/rare/lid.png", false);

        plugin.saveResource("skins/generated_skins.skins", false);

        plugin.getLogger().info("generated default config");
    }
}
