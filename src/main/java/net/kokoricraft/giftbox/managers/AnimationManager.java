package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.AnimationPart;
import net.kokoricraft.giftbox.enums.BoxPart;
import net.kokoricraft.giftbox.objects.Animation;
import net.kokoricraft.giftbox.objects.AnimationFrame;
import net.kokoricraft.giftbox.objects.BoxParticle;
import net.kokoricraft.giftbox.objects.BoxSound;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Display;

import java.util.HashMap;
import java.util.Map;

public class AnimationManager {

    private final GiftBox plugin;
    private Map<String, Animation> animations = new HashMap<>();

    public AnimationManager(GiftBox plugin){
        this.plugin = plugin;
    }

    public void play(String name, Display body, Display lid){
        Animation animation = animations.get(name);
        if(animation == null){
            Bukkit.broadcastMessage("animacion nula");
            return;
        }

        animation.play(body, lid);
    }

    public void initAnimations(){
        Animation da = new Animation(plugin);

        //animation to down and expand for sides
        AnimationFrame frame1 = new AnimationFrame(plugin, 60, 5, BoxPart.BODY);
        frame1.setScale(1.1, 0.6, 1.1);
        frame1.setTranslation(0, -0.1, 0);
        frame1.addSound(new BoxSound(Sound.ITEM_CROSSBOW_LOADING_END, 1f, 4f));

        AnimationFrame frame1b = new AnimationFrame(plugin, 60, 5, BoxPart.LID);
        frame1b.setScale(1.1 , 0.3, 1.1);
        frame1b.setTranslation(0, -0.1, 0);

        //animation to up and contract for sides
        AnimationFrame frame2 = new AnimationFrame(plugin, 65, 5, BoxPart.BODY);
        frame2.setScale(.9, 1, .9);
        frame2.setTranslation(0, 0.1, 0);
        frame2.addSound(new BoxSound(Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f, 4f), new BoxSound(Sound.ENTITY_ALLAY_HURT, 1f, 2f));
        frame2.addParticles(new BoxParticle(Particle.DRAGON_BREATH, .6, 8));

        AnimationFrame frame2b = new AnimationFrame(plugin, 65, 5, BoxPart.LID);
        frame2b.setScale(.9 , 0.3, .9);
        frame2b.setTranslation(0, 0.1, 0);

        //animation to reset box
        AnimationFrame frame3 = new AnimationFrame(plugin, 70, 3, BoxPart.BODY);
        frame3.setScale(1, .8, 1);
        frame3.setTranslation(0, 0, 0);

        AnimationFrame frame3b = new AnimationFrame(plugin, 70, 3, BoxPart.LID);
        frame3b.setScale(1 , 0.3, 1);
        frame3b.setTranslation(0, 0, 0);

        //animation to down and expand for sides
        AnimationFrame frame4 = new AnimationFrame(plugin, 80, 5, BoxPart.BODY);
        frame4.setScale(1.1, 0.6, 1.1);
        frame4.setTranslation(0, -0.1, 0);
        frame4.addSound(new BoxSound(Sound.ITEM_CROSSBOW_LOADING_END, 1f, 4f));

        AnimationFrame frame4b = new AnimationFrame(plugin, 80, 5, BoxPart.LID);
        frame4b.setScale(1.1 , 0.3, 1.1);
        frame4b.setTranslation(0, -0.1, 0);

        //animation to up and contract for sides
        AnimationFrame frame5 = new AnimationFrame(plugin, 85, 5, BoxPart.BODY);
        frame5.setScale(.9, 1, .9);
        frame5.setTranslation(0, 0.1, 0);
        frame5.addSound(new BoxSound(Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f, 4f), new BoxSound(Sound.ENTITY_ALLAY_HURT, 1f, 2f));
        frame5.addParticles(new BoxParticle(Particle.DRAGON_BREATH, .6, 8));

        AnimationFrame frame5b = new AnimationFrame(plugin, 85, 5, BoxPart.LID);
        frame5b.setScale(.9 , 0.3, .9);
        frame5b.setTranslation(0, 0.1, 0);

        //animation to reset box
        AnimationFrame frame6 = new AnimationFrame(plugin, 90, 3, BoxPart.BODY);
        frame6.setScale(1, .8, 1);
        frame6.setTranslation(0, 0, 0);

        AnimationFrame frame6b = new AnimationFrame(plugin, 90, 3, BoxPart.LID);
        frame6b.setScale(1 , 0.3, 1);
        frame6b.setTranslation(0, 0, 0);

        //animation to down and expand for sides
        AnimationFrame frame7 = new AnimationFrame(plugin, 100, 5, BoxPart.BODY);
        frame7.setScale(1.1, 0.6, 1.1);
        frame7.setTranslation(0, -0.1, 0);
        frame7.addSound(new BoxSound(Sound.ITEM_CROSSBOW_LOADING_END, 1f, 4f));

        AnimationFrame frame7b = new AnimationFrame(plugin, 100, 5, BoxPart.LID);
        frame7b.setScale(1.1 , 0.3, 1.1);
        frame7b.setTranslation(0, -0.1, 0);

        //animation to up and contract for sides
        AnimationFrame frame8 = new AnimationFrame(plugin, 105, 5, BoxPart.BODY);
        frame8.setScale(.8, 1.4, .8);
        frame8.setTranslation(0, 0.4, 0);//0.1
        frame8.addSound(new BoxSound(Sound.ENTITY_CHICKEN_EGG, 1f, 1f));
        frame8.addParticles(new BoxParticle(Particle.CLOUD, .15, 2, 0, 0.1, 0));

        AnimationFrame frame8b = new AnimationFrame(plugin, 105, 5, BoxPart.LID);
        frame8b.setScale(.8 , 0.3, .8);
        frame8b.setTranslation(0, 0.4, .3);
        frame8b.setRotation(1, 0, 0, 70);

        //animation to reset
        AnimationFrame frame9 = new AnimationFrame(plugin, 110, 3, BoxPart.BODY);
        frame9.setScale(1, .8, 1);
        frame9.setTranslation(0, 0, 0);

        AnimationFrame frame9b = new AnimationFrame(plugin, 110, 3, BoxPart.LID);
        frame9b.setScale(1 , 0.3, 1);
        frame9b.setTranslation(0, 0, .3);

        addFrames(da, frame1, frame1b, frame2, frame2b, frame3, frame3b, frame4, frame4b, frame5, frame5b, frame6, frame6b, frame7, frame7b, frame8, frame8b, frame9, frame9b);
        animations.put("default", da);
    }

    private void addFrames(Animation animation, AnimationFrame... frames){
        for(AnimationFrame frame : frames)
            animation.addFrame(frame);
    }
}
