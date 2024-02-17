package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.BoxParticle;
import net.kokoricraft.giftbox.objects.BoxType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class Manager {
    private final GiftBox plugin;
    private final Map<Item, BoxParticle> trait_items = new HashMap<>();
    private Map<String, BoxType> boxes = new HashMap<>();

    public Manager(GiftBox plugin){
        this.plugin = plugin;
    }

    public void place(Block block){

    }

    private BukkitTask trait_task;

    public void dropItem(ItemStack itemStack, Location location, BlockFace direction){
        World world = location.getWorld();
        if(world == null) return;


        BoxParticle particle = new BoxParticle(Particle.REDSTONE, Color.fromBGR(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)), 1f);
        Item item = world.dropItem(location, itemStack);
        trait_items.put(item, particle);

        Vector velocity = new Vector(0, 0.3, 0);

        switch(direction){
            case NORTH, SOUTH -> velocity.setX(getVelocityRandomDirection());
            case WEST, EAST -> velocity.setZ(getVelocityRandomDirection());
        }

        item.setVelocity(velocity);

        if(trait_task != null && !trait_task.isCancelled()) return;

        trait_task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                if(trait_items.isEmpty()) {
                    if(trait_task != null && !trait_task.isCancelled())
                        trait_task.cancel();

                    trait_task = null;
                    return;
                }

                Map<Item, BoxParticle> items = new HashMap<>(trait_items);

                for(Item item : items.keySet()){
                    BoxParticle boxParticle = items.get(item);
                    boxParticle.play(item.getLocation().add(0, 0.3, 0));
                    if(item.isOnGround() || boxParticle.getCounter() > 60){
                        Manager.this.trait_items.remove(item);
                    }
                }
            }
        }, 0, 0);
    }

    private final Random velocity_random = new Random();

    private double getVelocityRandomDirection(){
        Double[] directions = new Double[]{.1, -.1};
        return directions[velocity_random.nextInt(directions.length)];
    }

    public void setBoxes(HashMap<String, BoxType> boxes){
        this.boxes = boxes;
    }

    public void addBoxe(String name, BoxType type){
        boxes.put(name, type);
    }

    public Collection<String> getBoxesNames(){
        return boxes.keySet();
    }

    public BoxType getBoxType(String name) {
        return boxes.get(name);
    }
}
