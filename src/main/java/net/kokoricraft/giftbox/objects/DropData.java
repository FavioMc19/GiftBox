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
    private boolean pickup_only_owner = false;
    private boolean vector_to_player = false;

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
        if(north.isEmpty()) return new Vector(0, 0, 0);
        return north.get(random.nextInt(north.size()));
    }

    public Vector getSouth(){
        if(south.isEmpty()) return new Vector(0, 0, 0);
        return south.get(random.nextInt(south.size()));
    }

    public Vector getWest(){
        if(west.isEmpty()) return new Vector(0, 0, 0);
        return west.get(random.nextInt(west.size()));
    }

    public Vector getEast(){
        if(east.isEmpty()) return new Vector(0, 0, 0);
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

    public boolean isPickupOnlyOwner(){
        return pickup_only_owner;
    }

    public void setPickupOnlyOwner(boolean pickup_only_owner){
        this.pickup_only_owner = pickup_only_owner;
    }

    public boolean isVectorToPlayer(){
        return vector_to_player;
    }

    public void setVectorToPlayer(boolean vector_to_player){
        this.vector_to_player = vector_to_player;
    }
}
