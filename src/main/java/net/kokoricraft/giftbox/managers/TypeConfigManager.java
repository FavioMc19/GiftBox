package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.BoxItem;
import net.kokoricraft.giftbox.objects.BoxType;
import net.kokoricraft.giftbox.objects.NekoConfig;
import net.kokoricraft.giftbox.objects.NekoItem;
import org.bukkit.configuration.ConfigurationSection;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TypeConfigManager {
    private final GiftBox plugin;

    public TypeConfigManager(GiftBox plugin){
        this.plugin = plugin;
    }

    public final Map<String, NekoConfig> items_config = new HashMap<>();

    public void loadTypes(){
        File folder = new File(plugin.getDataFolder() + "/boxes");

        int count = 0;

        if(folder.exists()){
            File[] files = folder.listFiles();

            if(files == null) return;

            for(File file : files){
                if(file.isDirectory())
                    continue;

                if(!file.getName().toLowerCase().endsWith(".yml"))
                    continue;

                NekoConfig config = new NekoConfig("boxes/"+file.getName(), plugin);

                loadType(config, file.getName().replaceAll(".yml", ""));
                count++;
            }
        }

        plugin.getLogger().info(count+" GiftBoxes loaded");
    }

    public void loadItems(){
        for(String name : plugin.getManager().getBoxesNames()){
            File file = new File(plugin.getDataFolder() + "/items/"+name+".yml");
            NekoConfig config = new NekoConfig("items/"+file.getName(), plugin);
            loadItems(config, name);
        }
    }

    public void createType(String name){
        NekoConfig.saveFile("default-box.yml", plugin.getDataFolder() + "/boxes/" + name + ".yml", plugin);
        NekoConfig config = new NekoConfig("boxes/"+name+".yml", plugin);

        NekoItem item = new NekoItem(plugin, name, config.getConfigurationSection("item"));
        BoxType boxType = plugin.getManager().getBoxType(name);
        boxType.setItem(item);
        boxType.setSkin(config.getString("skin"), "create type"+name);
        boxType.setDefaultItemColor(config.getString("default_item_color"));
        boxType.setPermissions(config.getConfigurationSection("permissions"));
    }

    public void addItem(String name, BoxItem item){
        NekoConfig config = items_config.get(name);
        String path = "items."+item.getID()+".";
        config.set(path+"chance", item.getChance());
        config.set(path+"color", item.getColor());
        config.set(path+"item", item.getItemStack());
        config.saveConfig();
    }

    public void updateItem(String name, BoxItem item) {
        addItem(name, item);
    }

    public void removeItem(String name, int id){
        NekoConfig config = new NekoConfig("items/"+name+".yml", plugin);
        config.set("items."+id, null);
        config.saveConfig();
    }

    private void loadType(NekoConfig config, String name){
        try{
            BoxType boxType = new BoxType(plugin, name);
            plugin.getManager().addBox(name, boxType);
            boxType.updateEditInventory();

            if(config.contains("item"))
                boxType.setItem(new NekoItem(plugin, name, config.getConfigurationSection("item")));

            if(config.contains("skin"))
                boxType.setSkin(config.getString("skin"), "load type "+name+ "path: "+config.getPath());

            if(config.contains("default_item_color"))
                boxType.setDefaultItemColor(config.getString("default_item_color"));

            if(config.contains("permissions"))
                boxType.setPermissions(config.getConfigurationSection("permissions"));
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void loadItems(NekoConfig config, String name){
        items_config.put(name, config);
        BoxType type = plugin.getManager().getBoxType(name);

        type.setIDCounter(config.getInt("id-counter", 0));
        if(!config.exists()) return;

        for(String stringID : config.getConfigurationSection("items").getKeys(false)){
            ConfigurationSection section = config.getConfigurationSection("items."+stringID);
            int id = Integer.parseInt(stringID);

            BoxItem item = new BoxItem(plugin, section.getDouble("chance"), section.getString("color"), section.getItemStack("item"));
            item.setID(id);
            type.addItem(item);
        }
        type.updateEditInventory();
    }
}