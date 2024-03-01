package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxPart;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;

import java.util.*;

public class Animation {

    private final GiftBox plugin;
    private final Map<String, PartData> parts = new HashMap<>();
    private DropData dropData;
    private final String name;

    public Animation(GiftBox plugin, String name){
        this.plugin = plugin;
        this.name = name;
    }

    public void addPart(PartData partData){
        parts.put(partData.getName(), partData);
    }

    public PartData getPartData(String name){
        return parts.get(name);
    }

    public Collection<String> getParts(){
        return parts.keySet();
    }

    private final List<AnimationFrame> frames = new ArrayList<>();

    public void addFrame(AnimationFrame frame){
        this.frames.add(frame);
    }

    public void play(Map<String, ItemDisplay> displayMap){
        for(AnimationFrame animationFrame : frames){
            AnimationFrame frame = animationFrame.clone();
            frame.play(displayMap.get(frame.getPart()));
        }
    }

    public void setDropData(DropData dropData){
        this.dropData = dropData;
    }

    public DropData getDropData() {
        return dropData;
    }

    public String getName() {
        return name;
    }
}
