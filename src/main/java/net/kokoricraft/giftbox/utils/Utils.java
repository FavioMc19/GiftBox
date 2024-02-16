package net.kokoricraft.giftbox.utils;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.UUID;

public class Utils {
    private final GiftBox plugin;

    public Utils(GiftBox plugin){
        this.plugin = plugin;
    }

    public ItemStack getHeadFromURL(String texture) {
        PlayerProfile profile =  Bukkit.createPlayerProfile(UUID.randomUUID(), "");
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        PlayerTextures textures = profile.getTextures();
        try{
            URL url = new URL(texture);
            textures.setSkin(url);
            profile.setTextures(textures);
        }catch(Exception ignored) {
        }

        assert meta != null;
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }
}
