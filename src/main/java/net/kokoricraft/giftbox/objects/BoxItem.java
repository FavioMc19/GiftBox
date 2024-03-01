package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoxItem {
    private final GiftBox plugin;
    private double chance;
    private int id;
    private String color;
    private final ItemStack itemStack;
    private UUID owner;

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
        lore.add(plugin.getUtils().color("&f&m---------------------------"));
        lore.add(plugin.getUtils().color("            &eChance: &f"+chance+"%"));
        lore.add(plugin.getUtils().color("                &eColor: "+color+"■"));
        lore.add(plugin.getUtils().color("&f&m---------------------------"));

        meta.setLore(lore);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER, id);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void setID(int id){
        this.id = id;
    }

    public void setChance(double chance){
        this.chance = chance;
    }

    public void increaseChance(double value){
        setChance(chance + value);
    }

    public void setColor(String color){
        this.color = color;
    }

    public void setOwner(UUID owner){
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }
}
