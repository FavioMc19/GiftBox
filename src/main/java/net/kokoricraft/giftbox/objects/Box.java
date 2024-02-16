package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxPart;
import net.kokoricraft.giftbox.enums.BoxSkins;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Box {
    private Map<BoxPart, ItemDisplay> displays = new HashMap<>();
    private Map<BoxPart, ItemStack> textures = new HashMap<>();
    private BoxSkins skin;

    private final String key;
    private final Location location;
    private final GiftBox plugin;
    private final Random random;
    public Box(String key, Location location, GiftBox plugin){
        this.random = new Random();
        this.key = key;
        this.location = new Location(location.getWorld(), location.getBlockX()+.5, location.getBlockY()+.3, location.getBlockZ()+.5);
        this.plugin = plugin;
    }

    public String getKey(){
        return key;
    }

    public void place(BlockFace direction){
        if(skin == null)
            skin = BoxSkins.DEFAULT;

        Objects.requireNonNull(location.getWorld()).playSound(location, Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 1f, 1f);

        int rotation = switch (direction){
            case SOUTH -> 180;
            case EAST -> 90;
            case WEST -> 270;
            default -> 0;
        };

        World world = location.getWorld();

        if(world == null) return;

        //place in world and save in placed file
        ItemDisplay body = world.spawn(location.clone().add(0, 0.1, 0), ItemDisplay.class);

        Transformation body_transformation = body.getTransformation();
        body_transformation.getScale().set(1f, 0.8f, 1f);
        body.setTransformation(body_transformation);
        body.setRotation(rotation, 0);
        displays.put(BoxPart.BODY, body);
        body.setItemStack(plugin.getUtils().getHeadFromURL(skin.getBody()));

        ItemDisplay lid = world.spawn(location.clone().add(0, 0.25, 0), ItemDisplay.class);

        Transformation lid_transformation = lid.getTransformation();
        lid_transformation.getScale().set(1f, 0.3f, 1f);
        lid.setTransformation(lid_transformation);
        lid.setRotation(rotation, 0);
        displays.put(BoxPart.LID, lid);
        lid.setItemStack(plugin.getUtils().getHeadFromURL(skin.getLid()));

        plugin.getAnimationManager().play("default", body, lid);

        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getManager().dropItem(new ItemStack(Material.DIAMOND, 1), location, direction), 105);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            BoxParticle particle = new BoxParticle(Particle.DRAGON_BREATH, 0.2, 8, 0, 0.3, 0);
            particle.play(location);
            body.remove();
            lid.remove();
        }, 180);
    }

    public void transformationLeft(Display entity, Vector axis, float angle) {
        entity.setInterpolationDuration(10);
        entity.setInterpolationDelay(-1);
        Transformation transformation = entity.getTransformation();
        transformation.getLeftRotation()
                .set(new AxisAngle4f(angle, (float) axis.getX(), (float) axis.getY(), (float) axis.getZ()));
        entity.setTransformation(transformation);
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

    private Random getRandom(){
        return random;
    }
}
