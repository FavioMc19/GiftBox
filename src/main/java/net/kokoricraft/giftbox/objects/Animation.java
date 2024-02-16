package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxPart;
import org.bukkit.entity.Display;

import java.util.ArrayList;
import java.util.List;

public class Animation {

    private final GiftBox plugin;

    public Animation(GiftBox plugin){
        this.plugin = plugin;
    }

    private final List<AnimationFrame> frames = new ArrayList<>();

    public void addFrame(AnimationFrame frame){
        this.frames.add(frame);
    }

    public void play(Display body, Display lid){
        for (AnimationFrame animationFrame : frames) {
            AnimationFrame frame = animationFrame.clone();
            if(frame.getPart().equals(BoxPart.BODY)){
                frame.play(body);
                continue;
            }
            frame.play(lid);
        }
    }
}
