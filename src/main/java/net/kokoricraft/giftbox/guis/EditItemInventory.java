package net.kokoricraft.giftbox.guis;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.BoxItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditItemInventory implements InventoryHolder {
    private GiftBox plugin;
    private final String type;
    private final BoxItem item;
    private final Inventory inventory;

    public EditItemInventory(GiftBox plugin, String type, BoxItem item){
        this.plugin = plugin;
        this.type = type;
        this.item = item;

        this.inventory = Bukkit.createInventory(this, InventoryType.DISPENSER, plugin.getUtils().color("&eEdit item"));

        ItemStack color = plugin.getUtils().getHeadFromURLDirect("41fe27a13c5fc17515cae695852716326b2b5df47d8d6b95a789ae38cac7b1");
        ItemStack plus = plugin.getUtils().getHeadFromURLDirect("9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777");
        ItemStack minus = plugin.getUtils().getHeadFromURLDirect("935e4e26eafc11b52c11668e1d6634e7d1d0d21c411cb085f9394268eb4cdfba");
        ItemStack x = plugin.getUtils().getHeadFromURLDirect("beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7");
        ItemStack back = plugin.getUtils().getHeadFromURLDirect("9dbd8ec46192a955c6bd651ab4d86f9867e4d4bd99af21ca92ec9d26f86da91");

        inventory.setItem(7, color);
        inventory.setItem(2, plus);
        inventory.setItem(5, minus);
        inventory.setItem(8, x);
        inventory.setItem(6, back);
        inventory.setItem(0, item.getItemStack());

        ItemStack magenta = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta meta = magenta.getItemMeta();

        assert  meta != null;
        meta.setDisplayName(plugin.getUtils().color("&c"));
        magenta.setItemMeta(meta);

        for(int slot : plugin.getUtils().getSlots("1, 3, 4")){
            inventory.setItem(slot, magenta);
        }
    }

    public void click(int slot){

    }

    public Inventory getInventory(){
        return inventory;
    }
}
