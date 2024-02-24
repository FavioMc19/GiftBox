package net.kokoricraft.giftbox.managers;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.guis.EditItemInventory;
import net.kokoricraft.giftbox.objects.Box;
import net.kokoricraft.giftbox.objects.BoxItem;
import net.kokoricraft.giftbox.objects.BoxParticle;
import net.kokoricraft.giftbox.objects.BoxType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class Manager {
    private final GiftBox plugin;
    private final Map<Item, BoxParticle> traitItems = new HashMap<>();
    private final List<Player> editingItemColorList = new ArrayList<>();
    private Map<String, BoxType> boxes = new HashMap<>();
    public Map<Player, EditItemInventory> editItemInventoryMap = new HashMap<>();

    public Manager(GiftBox plugin){
        this.plugin = plugin;
    }

    public void place(Block block, BoxType boxType, BlockFace blockFace){
        Box box = new Box(boxType.getName(), block.getLocation(), plugin);

        box.setSkin(plugin.getSkinsConfigManager().skins.get(boxType.getSkin()));
        box.setDefaultItemColor(boxType.getDefaultItemColor());
        box.place(blockFace.getOppositeFace());
    }
    private BukkitTask trait_task;

    public void dropItem(BoxItem boxItem, Location location, BlockFace direction){
        World world = location.getWorld();
        if(world == null) return;

        if(boxItem == null){
            world.playSound(location, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        BoxParticle particle = new BoxParticle(Particle.REDSTONE, plugin.getUtils().getColor(boxItem.getColor()), 0.5f);
        Item item = world.dropItem(location, boxItem.getItemStack().clone());
        traitItems.put(item, particle);

        Vector velocity = new Vector(0, 0.3, 0);

        switch(direction){
            case NORTH, SOUTH -> velocity.setX(getVelocityRandomDirection());
            case WEST, EAST -> velocity.setZ(getVelocityRandomDirection());
        }

        item.setVelocity(velocity);

        if(trait_task != null && !trait_task.isCancelled()) return;

        trait_task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if(traitItems.isEmpty()) {
                if(trait_task != null && !trait_task.isCancelled())
                    trait_task.cancel();

                trait_task = null;
                return;
            }

            Map<Item, BoxParticle> items = new HashMap<>(traitItems);

            for(Item item1 : items.keySet()){
                BoxParticle boxParticle = items.get(item1);
                boxParticle.play(item1.getLocation().add(0, 0.3, 0));

                if(item1.isOnGround() || boxParticle.getCounter() > 60){
                    Manager.this.traitItems.remove(item1);
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

    public void addBox(String name, BoxType type){
        boxes.put(name, type);
    }

    public Collection<String> getBoxesNames(){
        return boxes.keySet();
    }

    public BoxType getBoxType(String name) {
        return boxes.get(name);
    }

    public boolean isEditingColor(Player player){
        return editingItemColorList.contains(player);
    }

    public void setEditingColor(Player player, boolean editing){
        if(editing && !isEditingColor(player)){
            editingItemColorList.add(player);
            return;
        }

        if(!editing)
            editingItemColorList.remove(player);
    }
}
