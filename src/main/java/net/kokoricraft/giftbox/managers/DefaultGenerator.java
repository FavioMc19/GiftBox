package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;

import java.io.File;

public class DefaultGenerator {
    GiftBox plugin;

    public DefaultGenerator(GiftBox plugin){
        this.plugin = plugin;
    }

    public void check(){
        if(plugin.getConfigManager().getConfigVersion() == -1){
            plugin.saveResource("boxes/tuffgolem.yml", false);
            plugin.saveResource("animations/tuff_golem_animation.yml", false);
            plugin.saveResource("skins/tuff_golem/cloth.png", false);
            plugin.saveResource("skins/tuff_golem/head.png", false);
            plugin.saveResource("skins/tuff_golem/left_arm.png", false);
            plugin.saveResource("skins/tuff_golem/right_arm.png", false);
            plugin.saveResource("skins/tuff_golem/legs.png", false);
            plugin.saveResource("skins/tuff_golem/nose.png", false);
        }

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
