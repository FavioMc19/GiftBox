package net.kokoricraft.giftbox.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Utils {
    private final GiftBox plugin;
    private final Pattern hex_pattern = Pattern.compile("(&#|#)([A-Fa-f0-9]{6})");

    public Utils(GiftBox plugin){
        this.plugin = plugin;
    }
    private Boolean isV1_19;

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
        return ChatColor.translateAlternateColorCodes('&', colorHex(text));
    }

    public List<String> color(List<String> list){
        List<String> parsed = new ArrayList<>();

        for(String line : list)
            parsed.add(color(line));

        return parsed;
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

    private final Cache<File, String> hash_cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();

    public String getImageHash(File file) {
        String hash = hash_cache.getIfPresent(file);

        if(hash != null)
            return hash;

        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(fileBytes);
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String colorHex(String text) {
        StringBuilder message = new StringBuilder();

        Matcher matcher = hex_pattern.matcher(text);

        int index = 0;
        while(matcher.find()) {
            message.append(text, index, matcher.start()).append(net.md_5.bungee.api.ChatColor.of((matcher.group().startsWith("&") ? matcher.group().substring(1) : matcher.group())));
            index = matcher.end();
        }
        return message.append(text.substring(index)).toString();
    }

    public boolean isV19(){
        if(isV1_19 != null) return isV1_19;

        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        isV1_19 = version.contains("1_19");
        return  isV1_19;
    }

    public byte[] ImageListToByte(List<File> images){
        try{
            File temporalFile = File.createTempFile("images-"+System.currentTimeMillis(), ".zip");
            try (FileOutputStream fos = new FileOutputStream(temporalFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                for(File file : images){
                    zos.putNextEntry(new ZipEntry((file.getName())));
                    try(FileInputStream fis = new FileInputStream(file)){
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while((bytesRead = fis.read(buffer)) != -1){
                            zos.write(buffer, 0, bytesRead);
                        }
                    }
                    zos.closeEntry();
                }
            }

            return Files.readAllBytes(temporalFile.toPath());
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    private static void extractFilesFromByte(byte[] zipFileBytes, String destinationDirectory) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(zipFileBytes);
            try (ZipInputStream zis = new ZipInputStream(bais)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    String fileName = entry.getName();
                    File file = new File(destinationDirectory + File.separator + fileName);
                    new File(file.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                    zis.closeEntry();
                }
            }
            System.out.println("Files extracted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
