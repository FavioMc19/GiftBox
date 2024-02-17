package net.kokoricraft.giftbox.utils;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    public ItemStack getHeadFromURLDirect(String texture){
        return getHeadFromURL("http://textures.minecraft.net/texture/"+texture);
    }

    public String color(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void sendMessage(CommandSender sender, String message){
        sender.sendMessage(color(message));
    }

    public List<Integer> getSlots(String text) {
        List<Integer> endSlots = new ArrayList<>();
        text = text.replaceAll(" ", "");
        String[] slots = text.split(",");
        for(String value : slots) {
            if(value.contains("-")) {
                int start = Integer.parseInt(value.split("-")[0]);
                int end = Integer.parseInt(value.split("-")[1]);

                for(int i = start; i < end+1; i++)
                    if(!endSlots.contains(i))
                        endSlots.add(i);
            }else {
                if(!endSlots.contains(Integer.parseInt(value)))
                    endSlots.add(Integer.parseInt(value));
            }
        }

        return endSlots;
    }
}
