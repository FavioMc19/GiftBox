package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxPart;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.*;

public class Box {
    private final Map<String, ItemDisplay> displays = new HashMap<>();
    private final Map<String, ItemStack> textures = new HashMap<>();
    private BoxSkin skin;
    private Animation animation;

    private final String name;
    private final Location location;
    private final GiftBox plugin;
    private String default_item_color = "&7";
    private Player owner;
    private Location dropLocation;
    private Vector dropVector;
    private List<BoxItem> generated_items = new ArrayList<>();
    private boolean removed = false;
    private int dropped_items = 0;
    private BlockFace direction;
    public Box(String name, Location location, GiftBox plugin){
        this.name = name;
        this.location = location.clone().add(0.5, 0, 0.5);
        this.plugin = plugin;
    }

    public void place(BlockFace direction){
        this.direction = direction;
        Objects.requireNonNull(location.getWorld()).playSound(location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 1f, 1f);

        for(String partName : animation.getParts()){
            PartData partData = animation.getPartData(partName);
            if(partData.isTemporal()) continue;

            spawnPart(partData);
        }

        //place in world and save in placed file
        plugin.getAnimationManager().play(animation, this);

        DropData dropData = animation.getDropData();

        if(dropData != null){
            dropLocation = location.clone().add(dropData.getX(), dropData.getY(), dropData.getZ());

            dropVector = switch (direction){
                case NORTH -> dropData.getNorth();
                case SOUTH -> dropData.getSouth();
                case EAST -> dropData.getEast();
                default -> dropData.getWest();
            };

            Bukkit.getScheduler().runTaskLater(plugin, () ->{
                if(!removed) dropItem();
            }, dropData.getDelay());
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            BoxParticle particle = new BoxParticle(Particle.DRAGON_BREATH, 0.2, 8, 0, 0.3, 0);
            particle.play(location);
            remove();
        }, animation.getRemoveDelay());
    }

    public void spawnPart(String part_name){
        PartData partData = animation.getPartData(part_name);
        spawnPart(partData);
    }

    public void spawnPart(PartData partData){
        World world = location.getWorld();
        if(world == null) return;

        int rotation = switch (direction){
            case SOUTH -> 0;
            case EAST -> 270;
            case WEST -> 90;
            default -> 180;
        };

        try{
            Vector pos = getPartLocation(direction, partData.getlocationX(), partData.getlocationY(), partData.getlocationZ());
            ItemDisplay itemDisplay = world.spawn(location.clone().add(pos.getX(), pos.getY(), pos.getZ()), ItemDisplay.class);

            Transformation transformation = itemDisplay.getTransformation();
            transformation.getScale().set(partData.getScaleX(), partData.getScaleY(), partData.getScaleZ());
            itemDisplay.setTransformation(transformation);

            itemDisplay.setRotation(rotation, 0);

            if(partData.isTemporal()){
                String material = partData.getMaterial();
                if(material.startsWith("[dropitem]")){
                    String[] material_data = material.split(" ");
                    int index = material_data.length > 1 ? Integer.parseInt(material_data[1]) : 0;
                    itemDisplay.setItemStack(getItemStack(index));
                }else{
                    try{
                        itemDisplay.setItemStack(new ItemStack(Material.valueOf(material)));
                    }catch (Exception exception){
                        itemDisplay.setItemStack(new ItemStack(Material.STONE));
                    }
                }
            }else {
                itemDisplay.setItemStack(plugin.getUtils().getHeadFromURL(skin.getPart(partData.getName()).getUrl()));
            }

            if(partData.isGlowing()){
                itemDisplay.setGlowing(true);
                itemDisplay.setGlowColorOverride(plugin.getUtils().getColor(partData.getGlowColor()));
            }
            displays.put(partData.getName(), itemDisplay);

        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void setSkin(BoxSkin skin){
        this.skin = skin;

        for(String part : skin.getPartsNames()){
            SkinPart skinPart = skin.getPart(part);
            ItemStack itemStack = plugin.getUtils().getHeadFromURL(skinPart.getUrl());
            textures.put(part, itemStack);
            if(displays.containsKey(part))
                displays.get(part).setItemStack(itemStack);
        }
    }

    public void setOwner(Player player){
        this.owner = player;
    }

    public void setDefaultItemColor(String default_item_color){
        this.default_item_color = default_item_color;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public void remove(){
        Map<String, Display> map = new HashMap<>(displays);
        for(String part_name : map.keySet()){
            Display display = map.get(part_name);
            display.remove();
        }

        plugin.getManager().placed_boxes.remove(this);
        removed = true;
    }

    public void dropItem(){
        if(owner != null && animation.getDropData().isVectorToPlayer())
            dropVector = vectorToPlayer(owner, dropLocation);

        BoxItem boxItem = getBoxItem(dropped_items);

        plugin.getManager().dropItem(boxItem, dropLocation, dropVector);
        dropped_items++;
    }

    public void forceDropItem(){
        if(removed) return;
        Objects.requireNonNull(dropLocation.getWorld()).dropItem(dropLocation, getItemStack(0));
    }

    public BoxItem getBoxItem(int index){
        if(generated_items.size() <= index){
            BoxItem boxItem = plugin.getManager().getBoxType(name).selectRandomItem();

            if(boxItem != null && (boxItem.getColor() == null || boxItem.getColor().isEmpty() || boxItem.getColor().isBlank()))
                boxItem.setColor(default_item_color);

            if(owner != null && animation.getDropData().isPickupOnlyOwner())
                boxItem.setOwner(owner.getUniqueId());

            generated_items.add(boxItem);
        }

        return generated_items.get(index);
    }

    public ItemStack getItemStack(int index){
        BoxItem boxItem = getBoxItem(index);
        return boxItem.getItemStack();
    }

    public Map<String, ItemDisplay> getDisplays(){
        return displays;
    }

    private Vector getPartLocation(BlockFace direction, double x, double y, double z){
        Vector vector = new Vector(0, y, 0);

        if(plugin.getUtils().isV19())
            direction = direction.getOppositeFace();

        switch (direction){
            case NORTH -> {
                vector.setZ(-z);
                vector.setX(x);
            }
            case SOUTH -> {
                vector.setZ(z);
                vector.setX(-x);
            }
            case EAST -> {
                vector.setX(z);
                vector.setZ(x);
            }
            default -> {
                vector.setX(-z);
                vector.setZ(-x);
            }
        }
        return vector;
    }

    private Vector vectorToPlayer(Player player, Location location) {
        Location playerLocation = player.getLocation().clone();

        Vector direction = playerLocation.toVector().subtract(location.toVector()).normalize();

        playerLocation.subtract(direction.clone().multiply(0.5));

        Vector playerVector = playerLocation.toVector();
        Vector itemVector = location.toVector();

        double dx = playerVector.getX() - itemVector.getX();
        double dz = playerVector.getZ() - itemVector.getZ();
        double dy = playerVector.getY() - itemVector.getY();

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        double distance = player.getLocation().distance(location);

        double time = horizontalDistance / (distance < 6 ? 0.3 : 0.6);

        if (dy > 0)
            time += Math.sqrt(2 * dy / 0.08);

        double horizontalSpeedX = dx / time;
        double horizontalSpeedZ = dz / time;
        double verticalSpeedY = dy / time + 0.5 * 0.08 * time;

        return new Vector(horizontalSpeedX, verticalSpeedY, horizontalSpeedZ);
    }

    public Location getLocation(){
        return location;
    }
}
