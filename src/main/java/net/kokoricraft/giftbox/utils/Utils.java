package net.kokoricraft.giftbox.utils;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
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

    public Color getColor(String text){
        text = parsetVanillaColor(text);

        java.awt.Color javaCOlor = java.awt.Color.decode(text);
        return Color.fromRGB(javaCOlor.getRed(), javaCOlor.getGreen(), javaCOlor.getBlue());
    }

    public String parsetVanillaColor(String text){
        String[] colors = new String[] {"&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f"};
        String[] hexs = new String[] {"#00000", "#0000aa", "#00aa00", "#00aaaa", "#aa0000", "#aa00aa", "#ffaa00", "#aaaaaa",
                "#555555", "#5555ff", "#55ff55", "#55ffff", "#ff5555", "#ff55ff", "#ffff55", "#ffffff"};

        for(int i = 0; i < colors.length; i++){
            String color = colors[i];
            String hex = hexs[i];

            if(text.contains(color)) text = text.replace(color, hex);
        }
        return text;
    }
}
