package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;

import java.util.ArrayList;
import java.util.List;

public class AnimationFrame {
    private final GiftBox plugin;
    private final int delay;
    private final int duration;
    private  final String part;
    private Vector scale_vector;
    private Vector translation_vector;
    private Vector axis_vector;
    private Integer angle;
    private final List<BoxSound> sounds = new ArrayList<>();
    private final List<BoxParticle> particles = new ArrayList<>();


    public AnimationFrame(GiftBox plugin, int delay, int duration, String part){
        this.plugin = plugin;
        this.delay = delay;
        this.duration = duration;
        this.part = part;
    }

    public int getDelay(){
        return delay;
    }

    public int getDuration(){
        return duration;
    }

    public String getPart(){
        return part;
    }

    public void setScale(Vector vector){
        this.scale_vector = vector;
    }

    public void setScale(double x, double y, double z){
        setScale(new Vector(x, y, z));
    }

    public void setTranslation(Vector vector){
        this.translation_vector = vector;
    }

    public void setTranslation(double x, double y, double z){
        if(plugin.getUtils().isV19()){
            x = -x;
            z = -z;
        }

        setTranslation(new Vector(x, y, z));
    }

    public void setRotation(Vector axis, int angle){
        this.axis_vector = axis;
        this.angle = angle;
    }

    public void setRotation(int x, int y, int z, int angle){
        if(plugin.getUtils().isV19()){
            x = -x;
            z = -z;
        }
        setRotation(new Vector(x, y, z), angle);
    }

    public void addSound(BoxSound... sound){
        sounds.addAll(List.of(sound));
    }

    public void addSound(List<BoxSound> sound){
        BoxSound[] sounds = new BoxSound[sound.size()];
        for(int i = 0; i < sound.size(); i++)
            sounds[i] = sound.get(i);

        addSound(sounds);
    }

    public void addParticles(BoxParticle... particle){
        particles.addAll(List.of(particle));
    }

    public void addParticles(List<BoxParticle> particle){
        BoxParticle[] particles = new BoxParticle[particle.size()];
        for(int i = 0; i < particle.size(); i++)
            particles[i] = particle.get(i);

        addParticles(particles);
    }

    public void play(Display display){
        if(delay == 0){
            startPlaying(display);
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> startPlaying(display), delay);
    }

    private void startPlaying(Display  display){
        Transformation transformation = display.getTransformation();
        display.setInterpolationDelay(-1);
        display.setInterpolationDuration(duration);

        if(scale_vector != null){
            transformation.getScale().set(scale_vector.getX(), scale_vector.getY(), scale_vector.getZ());
        }


        if(translation_vector != null){
            transformation.getTranslation().set(translation_vector.getX(), translation_vector.getY(), translation_vector.getZ());
        }

        if(axis_vector != null){
            transformation.getLeftRotation().set(new AxisAngle4f((float) Math.toRadians(angle), axis_vector.getBlockX(), axis_vector.getBlockY(), axis_vector.getBlockZ()));
        }
        display.setTransformation(transformation);

        sounds.forEach(sound -> sound.play(display.getLocation()));
        particles.forEach(particle -> particle.play(display.getLocation()));
    }

    @Override
    public AnimationFrame clone(){
        AnimationFrame animationFrame = new AnimationFrame(plugin, delay, duration, part);
        if(scale_vector != null)
            animationFrame.setScale(scale_vector);

        if(translation_vector != null)
            animationFrame.setTranslation(translation_vector);

        if(axis_vector != null)
            animationFrame.setRotation(axis_vector, angle);

        if(!sounds.isEmpty())
            animationFrame.addSound(sounds);

        if(!particles.isEmpty())
            animationFrame.addParticles(particles);

        return animationFrame;
    }
}
