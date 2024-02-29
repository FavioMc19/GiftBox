package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;

import java.util.Collection;
import java.util.HashMap;

public class BoxSkin {
    private final String name;
    private final GiftBox plugin;
    private final HashMap<String, SkinPart> skins_parts = new HashMap<>();

    public BoxSkin(GiftBox plugin, String name){
        this.plugin = plugin;
        this.name = name;
    }

    public void addPart(SkinPart skinPart){
        skins_parts.put(skinPart.getName(), skinPart);
    }

    public boolean contains(String name){
        return skins_parts.containsKey(name);
    }

    public SkinPart getPart(String name){
        return skins_parts.get(name);
    }

    public Collection<String> getPartsNames(){
        return skins_parts.keySet();
    }

    public String getName(){
        return name;
    }

    public boolean isGenerating(){
        for(SkinGeneratorCache skinGeneratorCache : plugin.getSkinsConfigManager().waiting_generation){
            if(skinGeneratorCache.getSkin().equals(this)) return true;
        }
        return false;
    }
}
