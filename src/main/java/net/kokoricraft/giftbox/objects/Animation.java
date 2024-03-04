package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;

import java.util.*;

public class Animation {

    private final GiftBox plugin;
    private final Map<String, PartData> parts = new HashMap<>();
    private DropData dropData;
    private final String name;
    private int remove_delay;
    private LinkedList<AnimationFrame> frames;

    public Animation(GiftBox plugin, String name){
        this.plugin = plugin;
        this.name = name;
        frames = new LinkedList<>();
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

    public void addFrame(AnimationFrame frame){
        this.frames.add(frame);
    }

    public void addFrames(LinkedList<AnimationFrame> list){
        this.frames = list;
    }

    public void play(Box box){
        for(AnimationFrame animationFrame : this.frames){
            AnimationFrame frame = animationFrame.clone();

            frame.play(frame.getPart(), box);
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

    public void setRemoveDelay(int delay){
        this.remove_delay = delay;
    }

    public int getRemoveDelay(){
        return remove_delay;
    }
}
