package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;

import java.util.HashMap;

public class BoxSkin {
    private final String name;
    private final GiftBox plugin;
    private final HashMap<String, SkinPart> skins_parts = new HashMap<>();

    public BoxSkin(GiftBox plugin, String name){
        this.plugin = plugin;
        this.name = name;
    }

    public void addSkinPart(SkinPart skinPart){
        skins_parts.put(skinPart.getName(), skinPart);
    }

    public boolean containsPart(String name){
        return skins_parts.containsKey(name);
    }

    public SkinPart getSkinPart(String name){
        return skins_parts.get(name);
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
