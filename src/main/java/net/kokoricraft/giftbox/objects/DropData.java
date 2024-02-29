package net.kokoricraft.giftbox.objects;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DropData {
    private final int delay;
    private final double x;
    private final double y;
    private final double z;
    private final List<Vector> north;
    private final List<Vector> south;
    private final List<Vector> west;
    private final List<Vector> east;
    private final Random random;

    public DropData(int delay, double x, double y, double z, List<Vector> north, List<Vector> south, List<Vector> west, List<Vector> east) {
        this.delay = delay;
        this.x = x;
        this.y = y;
        this.z = z;
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
        this.random = new Random();
    }

    public Vector getNorth(){
        return north.get(random.nextInt(north.size()));
    }

    public Vector getSouth(){
        return south.get(random.nextInt(south.size()));
    }

    public Vector getWest(){
        return west.get(random.nextInt(west.size()));
    }

    public Vector getEast(){
        return east.get(random.nextInt(east.size()));
    }

    public int getDelay() {
        return delay;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
