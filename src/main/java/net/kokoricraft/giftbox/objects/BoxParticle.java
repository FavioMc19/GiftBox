package net.kokoricraft.giftbox.objects;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BoxParticle {
    private final Particle particle;
    private double range;
    private final float size;
    private double x_relative = 0;
    private double y_relative = 0;
    private double z_relative = 0;
    private Color color;
    private int counter = 0;
    private Location from;

    public BoxParticle(Particle particle, double range, int size){
        this.particle = particle;
        this.range = range;
        this.size = size;
    }

    public BoxParticle(Particle particle, double range, int size, double x_relative, double y_relative, double z_relative){
        this(particle, range, size);
        this.x_relative = x_relative;
        this.y_relative = y_relative;
        this.z_relative = z_relative;
    }

    public BoxParticle(Particle particle, Color color, float size){
        this.particle = particle;
        this.color = color;
        this.size = size;
    }

    public void play(Location center){
        World world = center.getWorld();
        if(world == null) return;

        counter++;
        if(color != null){
            Particle.DustOptions dustOptions = new Particle.DustOptions(color, size);
            world.spawnParticle(Particle.REDSTONE, center, 0, dustOptions);
            if(from != null){
                for(Location location : getIntermediateLocations(center, from, 0.05)){
                    world.spawnParticle(Particle.REDSTONE, location, 0, dustOptions);
                }
                from = center;
            }else{
                from = center;
            }
            return;
        }
        getLocationsInRange(center.clone().add(x_relative, y_relative, z_relative)).forEach(location -> world.spawnParticle(particle, location, 0));
    }

    private List<Location> getLocationsInRange(Location center) {
        List<Location> locations = new ArrayList<>();
        World world = center.getWorld();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            double x = center.getX() + (random.nextDouble() * range * 2) - range;
            double y = center.getY() + (random.nextDouble() * range * 2) - range;
            double z = center.getZ() + (random.nextDouble() * range * 2) - range;

            Location location = new Location(world, x, y, z);
            locations.add(location);
        }

        return locations;
    }

    public int getCounter(){
        return counter;
    }

    private List<Location> getIntermediateLocations(Location loc1, Location loc2, double distance) {
        List<Location> intermediateLocations = new ArrayList<>();

        Vector vectorBetween = loc2.toVector().subtract(loc1.toVector());
        double totalDistance = vectorBetween.length();
        vectorBetween.normalize();

        double currentDistance = distance;

        while (currentDistance < totalDistance) {
            Vector intermediateVector = loc1.toVector().clone().add(vectorBetween.clone().multiply(currentDistance));
            Location intermediateLocation = intermediateVector.toLocation(Objects.requireNonNull(loc1.getWorld()));
            intermediateLocations.add(intermediateLocation);
            currentDistance += distance;
        }

        return intermediateLocations;
    }
}
