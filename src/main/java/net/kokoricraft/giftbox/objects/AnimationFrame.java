package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

public class AnimationFrame implements Cloneable{
    private final GiftBox plugin;
    private final int delay;
    private final int duration;
    private  final String part;
    private Vector scale_vector;
    private Vector translation_vector;
    private Vector axis_vector;
    private Integer angle;
    private String spawn_part;
    private String remove_part;
    private final List<BoxSound> sounds = new ArrayList<>();
    private final List<BoxParticle> particles = new ArrayList<>();
    private Box box;
    private boolean isPlayed = false;

    public AnimationFrame(GiftBox plugin, int delay, int duration, String part){
        this.plugin = plugin;
        this.delay = delay;
        this.duration = duration;
        this.part = part;
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

    public void setSpawn(String part){
        this.spawn_part = part;
    }

    public void setRemove(String part){
        this.remove_part = part;
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

    public void play(String part_name, Box box){
        this.box = box;
        if(delay == 0){
            startPlaying(part_name);
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> startPlaying(part_name), delay);
    }

    private void startPlaying(String part_name){
        ItemDisplay display = box.getDisplays().get(part_name);

        if(remove_part != null){
            ItemDisplay remove_display = box.getDisplays().get(remove_part);
            if(remove_display != null) remove_display.remove();
            return;
        }

        if(display == null) return;
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
            Quaternionf currentRotation = transformation.getLeftRotation();
            Quaternionf newRotation = new Quaternionf().rotationAxis(new AxisAngle4f((float) Math.toRadians(angle), axis_vector.getBlockX(), axis_vector.getBlockY(), axis_vector.getBlockZ()));
            currentRotation.mul(newRotation);
            transformation.getLeftRotation().set(currentRotation);
        }
        display.setTransformation(transformation);

        if(spawn_part != null)
            box.spawnPart(spawn_part);

        new BukkitRunnable() {
            @Override
            public void run() {
                sounds.forEach(sound -> sound.play(display.getLocation()));
                particles.forEach(particle -> particle.play(display.getLocation()));
            }
        }.runTaskLater(plugin, 1);
        isPlayed = true;
    }

    public void setPlayed(boolean played){
        this.isPlayed = played;
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

        if(spawn_part != null)
            animationFrame.setSpawn(spawn_part);

        if(remove_part != null)
            animationFrame.setRemove(remove_part);

        animationFrame.setPlayed(isPlayed);

        return animationFrame;
    }

    @Override
    public String toString(){
        return "{part:"+this.part+"delay:"+delay+" duration:"+duration+" scale:"+scale_vector+" translation:"+translation_vector+" axis:"+axis_vector+" angle:"+angle+" sound: "+sounds.size()+" particle:"+particles.size()+" spawn_part:"+spawn_part+"}";
    }

}
