package net.kokoricraft.giftbox.objects;

import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class SkinPart {
    private  String hash;
    private String name;
    private String url;
    private File file;

    public SkinPart(String name, String hash, String url){
        this.name = name;
        this.hash = hash;
        this.url = url;
    }

    public SkinPart(ConfigurationSection section){
        this.name = section.getName();
        if(section.contains("hash"))
            hash = section.getString("hash");

        if(section.contains("texture"))
            url = section.getString("texture");
    }

    public String getName(){
        return name;
    }

    public String getHash(){
        return hash;
    }

    public String getUrl(){
        return url;
    }

    public File getFile(){
        return file;
    }

    public void setFIle(File file) {
        this.file = file;
    }
}
