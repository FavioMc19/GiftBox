package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;
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
    private final Map<String, Animation> animations = new HashMap<>();

    public AnimationManager(GiftBox plugin){
        this.plugin = plugin;
    }

    public void play(String name, Display body, Display lid){
        Animation animation = animations.get(name);
        if(animation == null){
            return;
        }

        animation.play(body, lid);
    }

    public void initAnimations(){
        //initDefaultAnimation();
        initNewDefaultAnimation();
    }

    private void initNewDefaultAnimation(){
        Animation animation = new Animation(plugin);

        //animation to down and expand for sides
        addNewDefaultAnimationStep(animation, 0);
        addNewDefaultAnimationStep(animation, 25);
        addNewDefaultAnimationStep(animation, 50);
        addNewDefaultAnimationEnd(animation, 75);

        animations.put("default_animation", animation);
    }

    private void addNewDefaultAnimationEnd(Animation animation, int cooldown){
        //animation to down and expand for sides
        AnimationFrame frame1 = new AnimationFrame(plugin, 60 + cooldown, 5, BoxPart.BODY);
        frame1.setScale(1.3, 0.4, 1.3);
        frame1.setTranslation(0, -0.1, 0);
        frame1.addSound(new BoxSound(Sound.ITEM_CROSSBOW_LOADING_END, 1f, 4f));

        AnimationFrame frame1b = new AnimationFrame(plugin, 60 + cooldown, 5, BoxPart.LID);
        frame1b.setScale(1.3 , 0.3, 1.3);
        frame1b.setTranslation(0, -0.15, 0);

        //animation to up and contract for sides
        AnimationFrame frame2 = new AnimationFrame(plugin, 65 + cooldown, 5, BoxPart.BODY);
        frame2.setScale(0.8, 1.1, 0.8);
        frame2.setTranslation(0, 0.25, 0);
        frame2.addParticles(new BoxParticle(Particle.DRAGON_BREATH, .6, 8));
        frame2.addSound(new BoxSound(Sound.ENTITY_CHICKEN_EGG, 1f, 1f));

        AnimationFrame frame2b = new AnimationFrame(plugin, 65 + cooldown, 5, BoxPart.LID);
        frame2b.setScale(0.8, 0.3, 0.8);
        frame2b.setTranslation(0, 0.2, .3);

        frame2b.setRotation(1, 0, 0, 70);

        //reset box
        AnimationFrame frame3 = new AnimationFrame(plugin, 70 + cooldown, 3, BoxPart.BODY);
        frame3.setScale(1.04, .64, 1.04);
        frame3.setTranslation(0, 0, 0);

        AnimationFrame frame3b = new AnimationFrame(plugin, 70 + cooldown, 3, BoxPart.LID);
        frame3b.setScale(1.04 , 0.4, 1.04);
        frame3b.setTranslation(0, 0, .3);

        addFrames(animation, frame1, frame1b, frame2, frame2b, frame3, frame3b);
    }

    private void addNewDefaultAnimationStep(Animation animation, int cooldown){
        //animation to down and expand for sides
        AnimationFrame frame1 = new AnimationFrame(plugin, 60 + cooldown, 5, BoxPart.BODY);
        frame1.setScale(1.3, 0.4, 1.3);
        frame1.setTranslation(0, -0.1, 0);
        frame1.addSound(new BoxSound(Sound.ITEM_CROSSBOW_LOADING_END, 1f, 4f));

        AnimationFrame frame1b = new AnimationFrame(plugin, 60 + cooldown, 5, BoxPart.LID);
        frame1b.setScale(1.3 , 0.3, 1.3);
        frame1b.setTranslation(0, -0.15, 0);

        //animation to up and contract for sides
        AnimationFrame frame2 = new AnimationFrame(plugin, 65 + cooldown, 5, BoxPart.BODY);
        frame2.setScale(0.9, 0.8, 0.9);
        frame2.setTranslation(0, 0.1, 0);
        frame2.addSound(new BoxSound(Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f, 4f), new BoxSound(Sound.ENTITY_ALLAY_HURT, 1f, 2f));
        frame2.addParticles(new BoxParticle(Particle.DRAGON_BREATH, .6, 8));

        AnimationFrame frame2b = new AnimationFrame(plugin, 65 + cooldown, 5, BoxPart.LID);
        frame2b.setScale(0.9, 0.3, 0.9);
        frame2b.setTranslation(0, 0.05, 0);

        //animation to reset box
        AnimationFrame frame3 = new AnimationFrame(plugin, 70 + cooldown, 3, BoxPart.BODY);
        frame3.setScale(1.04, .64, 1.04);
        frame3.setTranslation(0, 0, 0);

        AnimationFrame frame3b = new AnimationFrame(plugin, 70 + cooldown, 3, BoxPart.LID);
        frame3b.setScale(1.04 , 0.4, 1.04);
        frame3b.setTranslation(0, 0, 0);

        addFrames(animation, frame1, frame1b, frame2, frame2b, frame3, frame3b);
    }

    private void addFrames(Animation animation, AnimationFrame... frames){
        for(AnimationFrame frame : frames)
            animation.addFrame(frame);
    }
}
