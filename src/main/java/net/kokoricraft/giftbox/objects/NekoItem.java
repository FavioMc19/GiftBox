package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public class NekoItem {
    private final GiftBox plugin;
    private final String boxType;

    private String name;
    private Integer custom_model_data;
    private List<String> lore;
    private Material material = Material.AMETHYST_SHARD;
    private ItemStack itemStack;
    private ItemStack head;

    public NekoItem(GiftBox plugin, String boxType, ConfigurationSection config){
        this.plugin = plugin;
        this.boxType = boxType;

        if(config.contains("name"))
            name = config.getString("name");

        if(config.contains("lore"))
            lore = config.getStringList("lore");

        if(config.contains("custom_model_data"))
            custom_model_data = config.getInt("custom_model_data");

        if(config.contains("material"))
            material = Material.valueOf(Objects.requireNonNull(config.getString("material")).toUpperCase());

        if(config.contains("texture") && material != null && material.equals(Material.PLAYER_HEAD))
            head = plugin.getUtils().getHeadFromURL(config.getString("texture"));
    }

    public ItemStack getItem(){
        if(itemStack != null) return itemStack;

        ItemStack itemStack = new ItemStack(material);

        if(head != null)
            itemStack = head;

        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        if(name != null)
            meta.setDisplayName(name);

        if(lore != null)
            meta.setLore(lore);

        if(custom_model_data != null)
            meta.setCustomModelData(custom_model_data);

        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "giftbox_id"), PersistentDataType.STRING, boxType);
        itemStack.setItemMeta(meta);
        this.itemStack = itemStack;

        return itemStack;
    }

    public static String getGiftBoxID(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return null;

        NamespacedKey key = new NamespacedKey(JavaPlugin.getPlugin(GiftBox.class), "giftbox_id");

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        if(!dataContainer.has(key)) return null;

        return dataContainer.get(key, PersistentDataType.STRING);
    }
}
