package net.kokoricraft.giftbox.objects;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public class BoxSound {
    private final float volume;
    private final float pitch;
    private final Sound sound;

    public BoxSound(Sound sound, float volume, float pitch){
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void play(Location location){
        World world = location.getWorld();
        if(world == null) return;

        world.playSound(location, sound, volume, pitch);
    }
}
