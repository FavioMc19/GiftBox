package net.kokoricraft.giftbox.listeners;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxSkins;
import net.kokoricraft.giftbox.guis.EditInventory;
import net.kokoricraft.giftbox.objects.Box;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
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
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !event.getBlockFace().equals(BlockFace.UP)) return;
        ItemStack itemStack = event.getItem();

        if(itemStack == null || !itemStack.getType().equals(Material.AMETHYST_SHARD)) return;
        Block block = Objects.requireNonNull(event.getClickedBlock()).getRelative(BlockFace.UP);

        Box box = new Box("test", block.getLocation(), plugin);
        box.setSkin(BoxSkins.DEFAULT);
        box.place(event.getPlayer().getFacing().getOppositeFace());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory inventory = event.getClickedInventory();
        if(inventory == null) return;

        if(!(inventory.getHolder() instanceof EditInventory editInventory)) return;

        switch (event.getSlot()){
            case 0 ->{
                editInventory.prevPage();
                event.getWhoClicked().openInventory(editInventory.getInventory());
            }
            case 45 ->{
                editInventory.nextPage();
                event.getWhoClicked().openInventory(editInventory.getInventory());
            }
        }
    }
}
