package net.kokoricraft.giftbox.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BoxItem {
    private final double chance;
    private final UUID uuid;
    private final String color;
    private final ItemStack itemStack;

    public BoxItem(UUID uuid, double chance, String color, ItemStack itemStack){
        this.uuid = uuid;
        this.chance = chance;
        this.color = color;
        this.itemStack = itemStack;
    }

    public double getChance() {
        return chance;
    }

    public UUID getUUID(){
        return uuid;
    }

    public String getColor(){
        return color;
    }

    public ItemStack getItemStack(){
        if(itemStack == null)
            return new ItemStack(Material.GOLDEN_SWORD);

        return itemStack;
    }
}
