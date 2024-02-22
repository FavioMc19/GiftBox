package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxPart;
import net.kokoricraft.giftbox.enums.BoxSkins;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Box {
    private final Map<BoxPart, ItemDisplay> displays = new HashMap<>();
    private final Map<BoxPart, ItemStack> textures = new HashMap<>();
    private BoxSkins skin;

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
        if(skin == null)
            skin = BoxSkins.DEFAULT;

        Location location = this.location.clone();
        location.add(.5, 0.3, .5);

        Objects.requireNonNull(location.getWorld()).playSound(location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 1f, 1f);

        int rotation = switch (direction){
            case SOUTH -> 0;
            case EAST -> 270;
            case WEST -> 90;
            default -> 180;
        };

        World world = location.getWorld();

        if(world == null) return;

        //place in world and save in placed file
        ItemDisplay body = world.spawn(location, ItemDisplay.class);

        Transformation body_transformation = body.getTransformation();
        body_transformation.getScale().set(1.04, 0.64, 1.04);
        body.setTransformation(body_transformation);
        body.setRotation(rotation, 0);
        displays.put(BoxPart.BODY, body);
        body.setItemStack(plugin.getUtils().getHeadFromURL(skin.getBody()));

        ItemDisplay lid = world.spawn(location.clone().add(0, 0.20, 0), ItemDisplay.class);

        Transformation lid_transformation = lid.getTransformation();
        lid_transformation.getScale().set(1.04, 0.4, 1.04);
        lid.setTransformation(lid_transformation);
        lid.setRotation(rotation, 0);
        displays.put(BoxPart.LID, lid);
        lid.setItemStack(plugin.getUtils().getHeadFromURL(skin.getLid()));

        plugin.getAnimationManager().play("default_animation", body, lid);

        BoxItem boxItem = plugin.getManager().getBoxType(name).selectRandomItem();

        if(boxItem != null && (boxItem.getColor() == null || boxItem.getColor().isEmpty() || boxItem.getColor().isBlank()))
            boxItem.setColor(default_item_color);

        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getManager().dropItem(boxItem, location, direction), 145);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            BoxParticle particle = new BoxParticle(Particle.DRAGON_BREATH, 0.2, 8, 0, 0.3, 0);
            particle.play(location);
            body.remove();
            lid.remove();
        }, 180);
    }

    public void setSkin(BoxSkins skin){
        this.skin = skin;

        ItemStack body = plugin.getUtils().getHeadFromURL(skin.getBody());
        ItemStack lid = plugin.getUtils().getHeadFromURL(skin.getLid());

        textures.put(BoxPart.BODY, body);
        textures.put(BoxPart.LID, lid);

        if(displays.containsKey(BoxPart.BODY))
            displays.get(BoxPart.BODY).setItemStack(body);

        if(displays.containsKey(BoxPart.LID))
            displays.get(BoxPart.LID).setItemStack(lid);
    }

    public void setDefaultItemColor(String default_item_color){
        this.default_item_color = default_item_color;
    }
}
