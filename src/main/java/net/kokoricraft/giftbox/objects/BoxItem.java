package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoxItem {
    private final GiftBox plugin;
    private final double chance;
    private int id;
    private final String color;
    private final ItemStack itemStack;

    public BoxItem(GiftBox plugin, double chance, String color, ItemStack itemStack){
        this.plugin = plugin;
        this.chance = chance;
        this.color = color;
        this.itemStack = itemStack;
    }

    public double getChance() {
        return chance;
    }

    public int getID(){
        return id;
    }

    public String getColor(){
        return color;
    }

    public ItemStack getItemStack(){
        if(itemStack == null)
            return new ItemStack(Material.GOLDEN_SWORD);

        return itemStack;
    }

    public ItemStack getIconItemStack(){
        ItemStack itemStack = getItemStack().clone();
        ItemMeta meta = itemStack.getItemMeta();
        assert  meta != null;

        List<String> lore = meta.getLore();
        if(lore == null)
            lore = new ArrayList<>();

        lore.add("");
        lore.add(plugin.getUtils().color("&echance: &f"+chance));
        lore.add(plugin.getUtils().color("&ecolor: "+color+"■"));

        meta.setLore(lore);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER, id);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void setID(int id){
        this.id = id;
    }
}
