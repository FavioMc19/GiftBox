package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.NekoConfig;

import java.io.File;

public class ConfigManager {
    private final GiftBox plugin;
    public NekoConfig config;
    private int config_version;
    public ConfigManager(GiftBox plugin){
        this.plugin = plugin;
    }

    public void loadConfig(){
        boolean exist = new File(plugin.getDataFolder()+"/config.yml").exists();
        config = new NekoConfig("config.yml", plugin);
        config_version = config.contains("config_version") ? config.getInt("config_version") : 0;

        if(!exist) config_version = -1;
    }

    public int getConfigVersion(){
        return config_version;
    }
}
