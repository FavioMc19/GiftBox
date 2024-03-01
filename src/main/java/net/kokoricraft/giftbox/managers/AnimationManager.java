package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationManager {

    //This class and system are a disaster.
    //When I come up with a better way to implement it, I'll change it.
    //If anyone wants to contribute, they can do so because I want this plugin to be for the community :)

    private final GiftBox plugin;
    public final Map<String, Animation> animations = new HashMap<>();

    public AnimationManager(GiftBox plugin){
        this.plugin = plugin;
    }

    public void play(Animation animation, Map<String, ItemDisplay> displayMap){
        if(animation == null) return;

        animation.play(displayMap);
    }

    public void initAnimations(){
        animations.clear();

        File animations_folder = new File(plugin.getDataFolder()+"/animations/");
        if(!animations_folder.exists())
            animations_folder.mkdirs();

        File[] files = animations_folder.listFiles();
        boolean defaultExist = false;

        if(files != null){
            for(File file : files){
                if(file.isDirectory() || !file.getName().endsWith(".yml")) continue;

                String name = file.getName().replace(".yml", "");
                if(name.equals("default_animation.yml")){
                    defaultExist = true;
                    initDefaultAnimation();
                    continue;
                }

                loadAnimation(new NekoConfig("animations/"+file.getName(), plugin));
            }
        }

        if(!defaultExist)
            initDefaultAnimation();
    }

    private void initDefaultAnimation(){
        NekoConfig default_animation = new NekoConfig("animations/default_animation.yml", plugin);

        if(default_animation.getString("init.parts.body.location", "").equals("x:0.5 y:0.3 z:0.5")){
            default_animation.set("init.parts.body.location", "x:0 y:0.3 z:0");
            default_animation.set("init.parts.lid.location", "x:0 y:0.5 z:0");
            default_animation.set("init.drop.location", "x:0 y:0.5 z:0");
            default_animation.saveConfig();
        }

        loadAnimation(default_animation);
    }

    public void loadAnimation(NekoConfig config){
        if(!config.contains("init")) return;
        String animation_name = config.getName();
        Animation animation = new Animation(plugin, animation_name);

        try{
            for(String part_name : config.getConfigurationSection("init.parts").getKeys(false)){
                ConfigurationSection section = config.getConfigurationSection("init.parts."+part_name);
                double location_x = 0, location_y = 0, location_z = 0, size_x = 1, size_y = 1, size_z = 1;

                if(section.contains("location")){
                    Map<String, String> location_data = getMap(section.getString("location", ""));
                    location_x = Double.parseDouble(location_data.getOrDefault("x", "0"));
                    location_y = Double.parseDouble(location_data.getOrDefault("y", "0"));
                    location_z = Double.parseDouble(location_data.getOrDefault("z", "0"));
                }

                if(section.contains("size")){
                    Map<String, String> location_data = getMap(section.getString("size", ""));
                    size_x = Double.parseDouble(location_data.getOrDefault("x", "0"));
                    size_y = Double.parseDouble(location_data.getOrDefault("y", "0"));
                    size_z = Double.parseDouble(location_data.getOrDefault("z", "0"));
                }

                PartData partData = new PartData(part_name, location_x, location_y, location_z, size_x, size_y, size_z);
                animation.addPart(partData);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }

        Map<String, String> dropMap = getMap(config.getString("init.drop.location"));

        int drop_delay = config.getInt("init.drop.delay", 0);

        double drop_x = Double.parseDouble(dropMap.getOrDefault("x", "0"));
        double drop_y = Double.parseDouble(dropMap.getOrDefault("y", "0"));
        double drop_z = Double.parseDouble(dropMap.getOrDefault("z", "0"));

        List<String> north = config.getStringList("init.drop.vector.north");
        List<String> south = config.getStringList("init.drop.vector.south");
        List<String> west = config.getStringList("init.drop.vector.west");
        List<String> east = config.getStringList("init.drop.vector.east");

        boolean pickup_only_owner = config.getBoolean("init.drop.pickup_only_owner", false);

        DropData dropData = new DropData(drop_delay, drop_x, drop_y,drop_z, getVectorList(north), getVectorList(south), getVectorList(west), getVectorList(east));
        dropData.setPickupOnlyOwner(pickup_only_owner);
        animation.setDropData(dropData);

        try{
            if(config.contains("frames")){
                for(String group_frame : config.getStringList("frames")){
                    int group_delay = 0;
                    String group = group_frame;
                    if(group_frame.contains(" ")){
                        group = group_frame.split(" ")[0];
                        group_delay = Integer.parseInt(group_frame.split(" ")[1]);
                    }

                    if(!config.contains("frames_groups."+group)) continue;
                    for(String step_id : config.getConfigurationSection("frames_groups."+group).getKeys(false)){
                        String path = "frames_groups."+group+"."+step_id+".";
                        int delay = config.getInt(path+"delay", 0) + group_delay;
                        int duration = config.getInt(path+"duration", 1);

                        if(config.contains(path+"parts")){
                            for(String part_name : config.getConfigurationSection(path+"parts").getKeys(false)){
                                AnimationFrame frame = new AnimationFrame(plugin, delay, duration, part_name);
                                for(String action_data : config.getStringList(path+"parts."+part_name+".actions")){

                                    String action = action_data.split(" ")[0];
                                    Map<String, String> data = getMap(action_data.replace(action+" ", ""));

                                    double x = Double.parseDouble(data.getOrDefault("x", "0"));
                                    double y = Double.parseDouble(data.getOrDefault("y", "0"));
                                    double z = Double.parseDouble(data.getOrDefault("z", "0"));

                                    switch (action.toLowerCase()){
                                        case "[scale]" ->{
                                            frame.setScale(x, y, z);
                                        }
                                        case "[translation]" ->{
                                            frame.setTranslation(x, y, z);
                                        }
                                        case "[sound]" ->{
                                            Sound sound = Sound.valueOf(data.get("sound").toUpperCase());
                                            float volume = Float.parseFloat(data.getOrDefault("volume", "1"));
                                            float pitch = Float.parseFloat(data.getOrDefault("pitch", "1"));
                                            frame.addSound(new BoxSound(sound, volume, pitch));
                                        }
                                        case "[particle]" ->{
                                            Particle particle = Particle.valueOf(data.getOrDefault("particle", "DRAGON_BREATH").toUpperCase());
                                            double range = Double.parseDouble(data.getOrDefault("range", "1"));
                                            int amount = Integer.parseInt(data.getOrDefault("amount", "1"));
                                            frame.addParticles(new BoxParticle(particle, range, amount, x, y, z));
                                        }
                                        case "[rotation]" ->{
                                            int angle = Integer.parseInt(data.getOrDefault("angle", "0"));
                                            frame.setRotation((int) x, (int) y, (int) z, angle);
                                        }
                                    }

                                    animation.addFrame(frame);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        animations.put(animation_name, animation);
    }

    private void initNewDefaultAnimation(){
        //Animation animation = new Animation(plugin);

        //animation to down and expand for sides
//        addNewDefaultAnimationStep(animation, 0);
//        addNewDefaultAnimationStep(animation, 25);
//        addNewDefaultAnimationStep(animation, 50);
//        addNewDefaultAnimationEnd(animation, 75);

        //animations.put("default_animation", animation);
    }

//    private void addNewDefaultAnimationEnd(Animation animation, int cooldown){
//        //animation to down and expand for sides
//        AnimationFrame frame1 = new AnimationFrame(plugin, 60 + cooldown, 5, BoxPart.BODY);
//        frame1.setScale(1.3, 0.4, 1.3);
//        frame1.setTranslation(0, -0.1, 0);
//        frame1.addSound(new BoxSound(Sound.ITEM_CROSSBOW_LOADING_END, 1f, 4f));
//
//        AnimationFrame frame1b = new AnimationFrame(plugin, 60 + cooldown, 5, BoxPart.LID);
//        frame1b.setScale(1.3 , 0.3, 1.3);
//        frame1b.setTranslation(0, -0.15, 0);
//
//        //animation to up and contract for sides
//        AnimationFrame frame2 = new AnimationFrame(plugin, 65 + cooldown, 5, BoxPart.BODY);
//        frame2.setScale(0.8, 1.1, 0.8);
//        frame2.setTranslation(0, 0.25, 0);
//        frame2.addParticles(new BoxParticle(Particle.DRAGON_BREATH, .6, 8));
//        frame2.addSound(new BoxSound(Sound.ENTITY_CHICKEN_EGG, 1f, 1f));
//
//        AnimationFrame frame2b = new AnimationFrame(plugin, 65 + cooldown, 5, BoxPart.LID);
//        frame2b.setScale(0.8, 0.3, 0.8);
//        frame2b.setTranslation(0, 0.2, .3);
//
//        frame2b.setRotation(1, 0, 0, 70);
//
//        //reset box
//        AnimationFrame frame3 = new AnimationFrame(plugin, 70 + cooldown, 3, BoxPart.BODY);
//        frame3.setScale(1.04, .64, 1.04);
//        frame3.setTranslation(0, 0, 0);
//
//        AnimationFrame frame3b = new AnimationFrame(plugin, 70 + cooldown, 3, BoxPart.LID);
//        frame3b.setScale(1.04 , 0.4, 1.04);
//        frame3b.setTranslation(0, 0, .3);
//
//        addFrames(animation, frame1, frame1b, frame2, frame2b, frame3, frame3b);
//    }
//
//    private void addNewDefaultAnimationStep(Animation animation, int cooldown){
//        //animation to down and expand for sides
//        AnimationFrame frame1 = new AnimationFrame(plugin, 60 + cooldown, 5, BoxPart.BODY);
//        frame1.setScale(1.3, 0.4, 1.3);
//        frame1.setTranslation(0, -0.1, 0);
//        frame1.addSound(new BoxSound(Sound.ITEM_CROSSBOW_LOADING_END, 1f, 4f));
//
//        AnimationFrame frame1b = new AnimationFrame(plugin, 60 + cooldown, 5, BoxPart.LID);
//        frame1b.setScale(1.3 , 0.3, 1.3);
//        frame1b.setTranslation(0, -0.15, 0);
//
//        //animation to up and contract for sides
//        AnimationFrame frame2 = new AnimationFrame(plugin, 65 + cooldown, 5, BoxPart.BODY);
//        frame2.setScale(0.9, 0.8, 0.9);
//        frame2.setTranslation(0, 0.1, 0);
//        frame2.addSound(new BoxSound(Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f, 4f), new BoxSound(Sound.ENTITY_ALLAY_HURT, 1f, 2f));
//        frame2.addParticles(new BoxParticle(Particle.DRAGON_BREATH, .6, 8));
//
//        AnimationFrame frame2b = new AnimationFrame(plugin, 65 + cooldown, 5, BoxPart.LID);
//        frame2b.setScale(0.9, 0.3, 0.9);
//        frame2b.setTranslation(0, 0.05, 0);
//
//        //animation to reset box
//        AnimationFrame frame3 = new AnimationFrame(plugin, 70 + cooldown, 3, BoxPart.BODY);
//        frame3.setScale(1.04, .64, 1.04);
//        frame3.setTranslation(0, 0, 0);
//
//        AnimationFrame frame3b = new AnimationFrame(plugin, 70 + cooldown, 3, BoxPart.LID);
//        frame3b.setScale(1.04 , 0.4, 1.04);
//        frame3b.setTranslation(0, 0, 0);
//
//        addFrames(animation, frame1, frame1b, frame2, frame2b, frame3, frame3b);
//    }

//    private void addFrames(Animation animation, AnimationFrame... frames){
//        for(AnimationFrame frame : frames)
//            animation.addFrame(frame);
//    }

    private Map<String, String> getMap(String input) {
        Map<String, String> resultMap = new HashMap<>();

        String[] parts = input.split("\\s+");
        for (String part : parts) {
            String[] keyValue = part.split(":");
            if (keyValue.length == 2) {
                resultMap.put(keyValue[0], keyValue[1]);
            }
        }

        return resultMap;
    }

    private List<Vector> getVectorList(List<String> input){
        List<Vector> vectors = new ArrayList<>();
        for(String line : input){
            Map<String, String> map = getMap(line);
            double x = Double.parseDouble(map.getOrDefault("x", "0"));
            double y = Double.parseDouble(map.getOrDefault("y", "0"));
            double z = Double.parseDouble(map.getOrDefault("z", "0"));
            vectors.add(new Vector(x, y, z));
        }
        return vectors;
    }
}
