package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.BoxItem;
import net.kokoricraft.giftbox.objects.BoxType;
import net.kokoricraft.giftbox.objects.NekoConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.UUID;

public class TypeConfigManager {
    private final GiftBox plugin;

    public TypeConfigManager(GiftBox plugin){
        this.plugin = plugin;
    }

    public void loadTypes(){
        File folder = new File(plugin.getDataFolder() + "/boxes");

        if(!folder.exists())
            folder.mkdirs();

        File[] files = folder.listFiles();

        if(files == null) return;

        int count = 0;

        for(File file : files){
            if(file.isDirectory())
                continue;

            if(!file.getName().toLowerCase().endsWith(".yml"))
                continue;

            NekoConfig config = new NekoConfig("boxes/"+file.getName(), plugin);

            loadType(config, file.getName().replaceAll(".yml", ""));
            count++;
        }

        plugin.getLogger().info(count+" GiftBoxes loaded");
    }

    public void loadItems(){
        for(String name : plugin.getManager().getBoxesNames()){
            File file = new File(plugin.getDataFolder() + "/items/"+name+".yml");
            if(!file.exists()) continue;

            NekoConfig config = new NekoConfig("items/"+file.getName(), plugin);
            loadItems(config, name);
        }
    }

    public void createType(String name){
        NekoConfig config = new NekoConfig("boxes/"+name+".yml", plugin);
        config.saveConfig();
    }

    public void addItem(String name, BoxItem item){
        NekoConfig config = new NekoConfig("items/"+name+".yml", plugin);
        String path = item.getUUID().toString()+".";
        config.set(path+"chance", item.getChance());
        config.set(path+"color", item.getColor());
        config.set(path+"item", item.getItemStack());
        config.saveConfig();
    }

    public void removeItem(String name, UUID uuid){
        NekoConfig config = new NekoConfig("items/"+name+".yml", plugin);
        config.set(uuid.toString(), null);
        config.saveConfig();
    }

    private void loadType(NekoConfig config, String name){
        BoxType boxType = new BoxType(plugin, name);
        plugin.getManager().addBox(name, boxType);
        boxType.updateEditInventory();
    }

    private void loadItems(NekoConfig config, String name){
        BoxType type = plugin.getManager().getBoxType(name);
        for(String stringUUID : config.getConfigurationSection("").getKeys(false)){
            ConfigurationSection section = config.getConfigurationSection(stringUUID);
            UUID uuid = UUID.fromString(stringUUID);

            BoxItem item = new BoxItem(plugin, uuid, section.getDouble("chance"), section.getString("color"), section.getItemStack("item"));
            type.addItem(item);
        }
    }
}
