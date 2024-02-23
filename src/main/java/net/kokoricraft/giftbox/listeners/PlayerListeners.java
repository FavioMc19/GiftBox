package net.kokoricraft.giftbox.listeners;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxSkins;
import net.kokoricraft.giftbox.guis.EditInventory;
import net.kokoricraft.giftbox.guis.EditItemInventory;
import net.kokoricraft.giftbox.objects.Box;
import net.kokoricraft.giftbox.objects.BoxSkin;
import net.kokoricraft.giftbox.objects.BoxType;
import net.kokoricraft.giftbox.objects.NekoItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Random;

public class PlayerListeners implements Listener {
    private final GiftBox plugin;

    public PlayerListeners(GiftBox plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        ItemStack itemStack = event.getItem();

        if(itemStack == null || itemStack.getType().equals(Material.AIR)) return;
        Block block = Objects.requireNonNull(event.getClickedBlock()).getRelative(BlockFace.UP);

        String name = NekoItem.getGiftBoxID(itemStack);
        if(name == null) return;

        BoxType boxType = plugin.getManager().getBoxType(name);
        if(boxType == null) return;
        event.setCancelled(true);

        if(!event.getBlockFace().equals(BlockFace.UP)) return;

        Player player = event.getPlayer();
        BoxSkin boxSkin = plugin.getSkinsConfigManager().skins.get(boxType.getSkin().toString().toLowerCase());

        if(boxSkin.isGenerating()){
            plugin.getUtils().sendMessage(player, "&cLa skin de esta giftbox se esta generando...");
            return;
        }

        if(boxType.isNeedPermission() && !player.hasPermission(boxType.getPermission())){
            plugin.getUtils().sendMessage(player, "&cNecesitas permiso para usar esta gift box");
            return;
        }

        plugin.getManager().place(block, boxType, player.getFacing().getOppositeFace());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory inventory = event.getInventory();
        if(event.getClickedInventory() == null) return;

        Player player = (Player) event.getWhoClicked();

        if(inventory.getHolder() instanceof EditInventory editInventory){
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);

            editInventory.click(event.getWhoClicked(), event.getSlot(), event.getClickedInventory().equals(event.getWhoClicked().getInventory()));

            Inventory newInventory = editInventory.getInventory();
            if(Objects.equals(event.getClickedInventory(), newInventory)) return;

            event.getWhoClicked().openInventory(newInventory);
            return;
        }

        EditItemInventory editItemInventory = plugin.getManager().editItemInventoryMap.get(player);

        if(editItemInventory != null && editItemInventory.getInventory().equals(event.getClickedInventory())){
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);

            editItemInventory.click(event.getWhoClicked(), event.getRawSlot(), event.isRightClick());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        if(!plugin.getManager().isEditingColor(player)) return;
        EditItemInventory editItemInventory = plugin.getManager().editItemInventoryMap.get(player);
        Bukkit.getScheduler().runTask(plugin, () -> editItemInventory.setItemColor(player, event.getMessage()));
        event.setCancelled(true);
    }
}
