package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxPart;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Box {
    private final Map<String, ItemDisplay> displays = new HashMap<>();
    private final Map<String, ItemStack> textures = new HashMap<>();
    private BoxSkin skin;
    private Animation animation;

    private final String name;
    private final Location location;
    private final GiftBox plugin;
    private String default_item_color = "&7";
    public Box(String name, Location location, GiftBox plugin){
        this.name = name;
        this.location = location;
        this.plugin = plugin;
    }

    public void place(BlockFace direction){
        Location location = this.location.clone();
        location.add(0.5, 0, 0.5);

        Objects.requireNonNull(location.getWorld()).playSound(location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 1f, 1f);

        int rotation = switch (direction){
            case SOUTH -> 0;
            case EAST -> 270;
            case WEST -> 90;
            default -> 180;
        };

        World world = location.getWorld();

        if(world == null) return;

        for(String partName : animation.getParts()){
            PartData partData = animation.getPartData(partName);
            try{
                Vector pos = getPartLocation(direction, partData.getlocationX(), partData.getlocationY(), partData.getlocationZ());
                ItemDisplay itemDisplay = world.spawn(location.clone().add(pos.getX(), pos.getY(), pos.getZ()), ItemDisplay.class);

                Transformation transformation = itemDisplay.getTransformation();
                transformation.getScale().set(partData.getScaleX(), partData.getScaleY(), partData.getScaleZ());
                itemDisplay.setTransformation(transformation);

                itemDisplay.setRotation(rotation, 0);
                displays.put(partName, itemDisplay);
                itemDisplay.setItemStack(plugin.getUtils().getHeadFromURL(skin.getPart(partName).getUrl()));
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }

        //place in world and save in placed file
        plugin.getAnimationManager().play(animation, displays);

        BoxItem boxItem = plugin.getManager().getBoxType(name).selectRandomItem();

        if(boxItem != null && (boxItem.getColor() == null || boxItem.getColor().isEmpty() || boxItem.getColor().isBlank()))
            boxItem.setColor(default_item_color);

        DropData dropData = animation.getDropData();

        if(dropData != null){
            Location dropLocation = location.clone().add(dropData.getX(), dropData.getY(), dropData.getZ());

            Vector velocity = switch (direction){
                case NORTH -> dropData.getNorth();
                case SOUTH -> dropData.getSouth();
                case EAST -> dropData.getEast();
                default -> dropData.getWest();
            };

            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getManager().dropItem(boxItem, dropLocation, velocity), dropData.getDelay());
        }



        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            BoxParticle particle = new BoxParticle(Particle.DRAGON_BREATH, 0.2, 8, 0, 0.3, 0);
            particle.play(location);
            Map<String, Display> map = new HashMap<>(displays);
            for(Display display : map.values()){
               display.remove();
            }
        }, 180);
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

    public void setDefaultItemColor(String default_item_color){
        this.default_item_color = default_item_color;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    private Vector getPartLocation(BlockFace direction, double x, double y, double z){
        Vector vector = new Vector(0, y, 0);
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
}
