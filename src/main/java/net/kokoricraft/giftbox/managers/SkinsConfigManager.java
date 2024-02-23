package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.BoxSkin;
import net.kokoricraft.giftbox.objects.NekoConfig;
import net.kokoricraft.giftbox.objects.SkinGeneratorCache;
import net.kokoricraft.giftbox.objects.SkinPart;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.mineskin.MineskinClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SkinsConfigManager {
    GiftBox plugin;

    public SkinsConfigManager(GiftBox plugin){
        this.plugin = plugin;
    }

    private NekoConfig generated_skins;
    public final HashMap<String, BoxSkin> skins = new HashMap<>();
    public final List<SkinGeneratorCache> waiting_generation = new ArrayList<>();

    public void loadSkins(){
        generated_skins = new NekoConfig("skins/generated_skins.skins", plugin);

        File skins_folder = new File(plugin.getDataFolder()+"/skins/");

        if(generated_skins.getConfigurationSection("") != null){
            for(String name : generated_skins.getConfigurationSection("").getKeys(false)){
                BoxSkin boxSkin = new BoxSkin(plugin, name);
                for(String part_name : generated_skins.getConfigurationSection(name).getKeys(false)){
                    ConfigurationSection configurationSection = generated_skins.getConfigurationSection(name+"."+part_name);
                    SkinPart skinPart = new SkinPart(configurationSection);
                    boxSkin.addSkinPart(skinPart);
                }
                skins.put(name, boxSkin);
            }
        }

        if(!skins_folder.exists())
            skins_folder.mkdirs();

        File[] folders = skins_folder.listFiles();

        if(folders == null) return;

        for(File folder : folders){
            if(!folder.isDirectory()) continue;
            String name = folder.getName();
            File[] parts_file = folder.listFiles();
            if(parts_file == null) continue;
            boolean exist = skins.containsKey(name);
            BoxSkin skin = skins.getOrDefault(name, new BoxSkin(plugin, name));

            if(!exist)
                skins.put(name, skin);

            for(File part_file : parts_file){
                if(part_file.isDirectory() || !part_file.getName().endsWith(".png")) continue;

                String part_name = part_file.getName().replace(".png", "");

                if(!skin.containsPart(part_name) || !skin.getSkinPart(part_name).getHash().equals(plugin.getUtils().getImageHash(part_file))){
                    waiting_generation.add(new SkinGeneratorCache(part_name, part_file, skin));
                    checkGeneration();
                }
            }
        }

    }

    private boolean generating = false;

    private void checkGeneration(){
        if(generating || waiting_generation.isEmpty()) return;

        generating = true;
        SkinGeneratorCache skinGeneratorCache = waiting_generation.get(0);
        genSkinPart(skinGeneratorCache);
    }

    private void genSkinPart(SkinGeneratorCache skinGeneratorCache){
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () ->{
            String hash = plugin.getUtils().getImageHash(skinGeneratorCache.getFile());
            MineskinClient client = new MineskinClient("MyUserAgent");
            try{
                client.generateUpload(skinGeneratorCache.getFile()).thenAccept(uploaded_skin ->{
                    String url = uploaded_skin.data.texture.url;
                    SkinPart skinPart = new SkinPart(skinGeneratorCache.getName(), hash, url);
                    skinGeneratorCache.getSkin().addSkinPart(skinPart);

                    generated_skins.set(skinGeneratorCache.getSkin().getName()+"."+skinGeneratorCache.getName()+".hash", hash);
                    generated_skins.set(skinGeneratorCache.getSkin().getName()+"."+skinGeneratorCache.getName()+".texture", url);
                    generated_skins.saveConfig();
                    generating = false;
                    waiting_generation.remove(skinGeneratorCache);
                    checkGeneration();
                });
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }, 20 * 5);
    }

}
