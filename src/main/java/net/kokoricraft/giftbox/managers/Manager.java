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
    public List<Box> placed_boxes = new ArrayList<>();
    private final Map<Block, Box> placedBoxesMap = new HashMap<>();
    public Map<Player, EditItemInventory> editItemInventoryMap = new HashMap<>();

    public Manager(GiftBox plugin){
        this.plugin = plugin;
    }

    public void place(Block block, BoxType boxType, BlockFace blockFace, Player owner){
        Box box = new Box(boxType.getName(), block.getLocation(), plugin);
        box.setOwner(owner);

        box.setSkin(plugin.getSkinsConfigManager().skins.get(boxType.getSkin()));
        box.setDefaultItemColor(boxType.getDefaultItemColor());
        box.setAnimation(plugin.getAnimationManager().animations.get(boxType.getAnimation()));

        if(plugin.getUtils().isV19())
            blockFace = blockFace.getOppositeFace();

        box.place(blockFace.getOppositeFace());
        placed_boxes.add(box);
        placedBoxesMap.put(block, box);
    }
    private BukkitTask trait_task;

    public void dropItem(BoxItem boxItem, Location location, Vector velocity){
        World world = location.getWorld();
        if(world == null) return;

        if(boxItem == null){
            world.playSound(location, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        BoxParticle particle = new BoxParticle(Particle.REDSTONE, plugin.getUtils().getColor(boxItem.getColor()), 0.5f);
        Item item = world.dropItem(location, boxItem.getItemStack().clone());

        UUID owner = boxItem.getOwner();

        if(owner != null) item.setOwner(owner);

        traitItems.put(item, particle);

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

    public void removeBox(String name) {
        boxes.remove(name);
        plugin.getTypeConfigManager().delete(name);
    }

    public boolean isBoxAtBlock(Block block){
        return placedBoxesMap.containsKey(block);
    }

    public void removePlacedBox(Box box){
        placed_boxes.remove(box);
        placedBoxesMap.remove(box.getLocation().getBlock());
    }
}
