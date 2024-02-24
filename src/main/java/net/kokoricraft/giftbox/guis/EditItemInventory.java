package net.kokoricraft.giftbox.guis;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.BoxItem;
import net.kokoricraft.giftbox.objects.BoxType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EditItemInventory implements InventoryHolder {
    private GiftBox plugin;
    private final String type;
    private final BoxItem item;
    private final Inventory inventory;

    ItemStack color;
    ItemStack plus;
    ItemStack minus;
    ItemStack x;
    ItemStack back;

    public EditItemInventory(GiftBox plugin, String type, BoxItem item){
        this.plugin = plugin;
        this.type = type;
        this.item = item;
        this.inventory = Bukkit.createInventory(this, InventoryType.DISPENSER, plugin.getUtils().color("&eEdit item"));

        ItemStack magenta = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta meta = magenta.getItemMeta();

        assert  meta != null;
        meta.setDisplayName(plugin.getUtils().color("&c"));
        magenta.setItemMeta(meta);

        for(int slot : plugin.getUtils().getSlots("1, 3, 4")){
            inventory.setItem(slot, magenta);
        }

        color = setColorLore(plugin.getUtils().getHeadFromURLDirect("41fe27a13c5fc17515cae695852716326b2b5df47d8d6b95a789ae38cac7b1"));
        plus = plugin.getUtils().getHeadFromURLDirect("9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777");
        minus = plugin.getUtils().getHeadFromURLDirect("935e4e26eafc11b52c11668e1d6634e7d1d0d21c411cb085f9394268eb4cdfba");
        x = setDeleteLore(plugin.getUtils().getHeadFromURLDirect("beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7"));
        back = setBackLore(plugin.getUtils().getHeadFromURLDirect("9dbd8ec46192a955c6bd651ab4d86f9867e4d4bd99af21ca92ec9d26f86da91"));

        update();
    }

    private void update(){
        inventory.setItem(7, setColorLore(color));
        inventory.setItem(2, setChanceLore(plus.clone(), true));
        inventory.setItem(5, setChanceLore(minus.clone(), false));
        inventory.setItem(8, x);
        inventory.setItem(6, back);
        inventory.setItem(0, item.getIconItemStack());

    }

    public void click(HumanEntity player, int slot, boolean right){
        boolean updateItem = false;
        BoxType boxType = plugin.getManager().getBoxType(type);

        switch (slot){
            case 6 ->{
                boxType.openEditInventory((Player) player);
            }
            case 8 ->{
                boxType.removeAndSave(item.getID());
                boxType.openEditInventory((Player) player);
            }
            case 2 ->{
                double increase = right ? .1 : 1;
                if(item.getChance()+increase <= 100){
                    item.increaseChance(increase);
                    updateItem = true;
                }else if(item.getChance() != 100){
                    item.setChance(100);
                    updateItem = true;
                }
            }
            case 5 ->{
                double increase = right ? -.1 : -1;
                if(item.getChance()+increase >= 0){
                    item.increaseChance(increase);
                    updateItem = true;
                }else if(item.getChance() != 0){
                    item.setChance(0);
                    updateItem = true;
                }
            }
            case 7 ->{
                plugin.getManager().setEditingColor((Player) player, true);
                player.closeInventory();
                player.sendMessage(plugin.getUtils().color("&eWrite the color for the item in the chat"));
            }
        }
        if(updateItem){
            update();
            boxType.update(item);
            boxType.getInventory().updateItem(item);
        }
        ((Player)player).playSound(player, Sound.UI_BUTTON_CLICK, 0.5f, 1f);
    }

    public Inventory getInventory(){
        return inventory;
    }

    private ItemStack setChanceLore(ItemStack item, boolean increase){
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return item;

        List<String> lore = new ArrayList<>();

        lore.add(plugin.getUtils().color("&f&m---------------------------"));
        lore.add(plugin.getUtils().color("           &eChance:&f "+this.item.getChance()+"%"));
        if(increase)
            lore.add(plugin.getUtils().color("&7[Left click] &a+1 &8| &7[Right click] &a+0.1"));
        if(!increase)
            lore.add(plugin.getUtils().color("&7[Left click] &c-1 &8| &7[Right click] &c-0.1"));
        lore.add(plugin.getUtils().color("&f&m---------------------------"));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack setDeleteLore(ItemStack itemStack){
        List<String> lore = new ArrayList<>();
        lore.add(plugin.getUtils().color("&f&m---------------------------"));
        lore.add(plugin.getUtils().color("             &c[Delete item]"));
        lore.add(plugin.getUtils().color("&f&m---------------------------"));
        return setLore(itemStack, lore);
    }

    private ItemStack setBackLore(ItemStack itemStack){
        List<String> lore = new ArrayList<>();
        lore.add(plugin.getUtils().color("&f&m---------------------------"));
        lore.add(plugin.getUtils().color("            &e[Back to menu]"));
        lore.add(plugin.getUtils().color("&f&m---------------------------"));
        return setLore(itemStack, lore);
    }

    private ItemStack setColorLore(ItemStack itemStack){
        List<String> lore = new ArrayList<>();
        lore.add(plugin.getUtils().color("&f&m---------------------------"));
        lore.add(plugin.getUtils().color("                &eColor: "+item.getColor()+"■"));
        lore.add(plugin.getUtils().color("           &7[Change color]"));
        lore.add(plugin.getUtils().color("&f&m---------------------------"));
        return setLore(itemStack, lore);
    }

    private ItemStack setLore(ItemStack itemStack, List<String> lore){
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return itemStack;
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void setItemColor(Player player, String message) {
        item.setColor(message);
        BoxType boxType = plugin.getManager().getBoxType(type);
        boxType.update(item);
        update();
        player.openInventory(getInventory());
    }
}
