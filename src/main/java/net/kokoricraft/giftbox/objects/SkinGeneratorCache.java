package net.kokoricraft.giftbox.objects;

import java.io.File;
import java.util.Objects;

public class SkinGeneratorCache {
     private final BoxSkin skin;
    private final String name;
    private final File file;
    private final String hash;

    public SkinGeneratorCache(String name, File file, BoxSkin skin, String hash){
        this.name = name;
        this.file = file;
        this.skin = skin;
        this.hash = hash;
    }

    public BoxSkin getSkin(){
        return skin;
    }

    public String getName(){
        return name;
    }

    public File getFile(){
        return file;
    }

    public String getHash(){
        return hash;
    }

    @Override
    public String toString(){
        return skin.getName()+"-"+file.getName()+"-"+name;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(skin.getName()+file.getName()+name);
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof SkinGeneratorCache)) return false;
        return object.toString().equals(toString());
    }
}
