package net.kokoricraft.giftbox.guis;

import com.google.common.collect.Lists;
import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.BoxItem;
import net.kokoricraft.giftbox.objects.BoxType;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class EditInventory implements InventoryHolder {
    private final GiftBox plugin;
    private final String type;
    private final List<Inventory> inventoryList = new ArrayList<>();
    private int index = 0;
    private final List<Integer> emptySlots;
    private final Map<Integer, Integer[]> assigned_items = new HashMap<>();

    public EditInventory(GiftBox plugin, String type){
        this.plugin = plugin;
        this.type = type;
        inventoryList.add(createInventory(1));
        emptySlots = plugin.getUtils().getSlots("2-7,11-16,20-25,29-34,38-43,47-52");
    }

    public void setItems(){
        assigned_items.clear();
        BoxType boxType = plugin.getManager().getBoxType(type);
        List<BoxItem> boxItems = boxType.getItems();
        boxItems.sort(new ItemComparator());
        List<List<BoxItem>> lists = Lists.partition(boxItems, 36);
        if(!lists.isEmpty())
            inventoryList.clear();

        for(int i = 0; i < lists.size(); i++){
            List<BoxItem> items = lists.get(i);
            Inventory inventory = createInventory(lists.size());
            inventoryList.add(inventory);
            if(items.isEmpty()) break;

            int counter = 0;
            for(int slot : emptySlots){
                if(items.size() <= counter) break;
                BoxItem boxItem = items.get(counter);
                inventory.setItem(slot, boxItem.getIconItemStack());
                assigned_items.put(boxItem.getID(), new Integer[]{i, slot});
                counter++;
            }
        }
    }

    private Inventory createInventory(int total){
        Inventory inventory = Bukkit.createInventory(this, 54, type+" "+(inventoryList.size()+1)+"/"+total);

        boolean up = !inventoryList.isEmpty();
        boolean down = total > inventoryList.size() +1;

        for(int slot : plugin.getUtils().getSlots("0, 9, 18, 27, 36, 45")){
            ItemStack itemStack = plugin.getUtils().getHeadFromURLDirect("a19d64612ba8d1c02ee270d84519ad0cd73175bc45e7dda3f639686b2ce64596");
            inventory.setItem(slot, itemStack);
        }

        if(up){
            ItemStack itemStack = plugin.getUtils().getHeadFromURLDirect("6ccbf9883dd359fdf2385c90a459d737765382ec4117b04895ac4dc4b60fc");
            ItemMeta meta = itemStack.getItemMeta();

            assert meta != null;
            meta.setDisplayName("Pagina anterior");
            itemStack.setItemMeta(meta);
            inventory.setItem(0, itemStack);
        }

        if(down){
            ItemStack itemStack = plugin.getUtils().getHeadFromURLDirect("72431911f4178b4d2b413aa7f5c78ae4447fe9246943c31df31163c0e043e0d6");
            ItemMeta meta = itemStack.getItemMeta();

            assert meta != null;
            meta.setDisplayName("Siguiente pagina");
            itemStack.setItemMeta(meta);
            inventory.setItem(45, itemStack);
        }

//        ItemStack color = plugin.getUtils().getHeadFromURLDirect("41fe27a13c5fc17515cae695852716326b2b5df47d8d6b95a789ae38cac7b1");
//        ItemStack plus = plugin.getUtils().getHeadFromURLDirect("9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777");
//        ItemStack minus = plugin.getUtils().getHeadFromURLDirect("935e4e26eafc11b52c11668e1d6634e7d1d0d21c411cb085f9394268eb4cdfba");
//        ItemStack x = plugin.getUtils().getHeadFromURLDirect("beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7");
//
//        inventory.setItem(9, color);
//        inventory.setItem(18, plus);
//        inventory.setItem(27, minus);
//        inventory.setItem(36, x);

        for(int slot : plugin.getUtils().getSlots("1, 8, 10, 17, 19, 26, 28, 35, 37, 44, 46, 53")){
            ItemStack itemStack = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
            ItemMeta meta = itemStack.getItemMeta();

            assert  meta != null;
            meta.setDisplayName(plugin.getUtils().color("&c"));
            itemStack.setItemMeta(meta);
            inventory.setItem(slot, itemStack);
        }
        return inventory;
    }

    public void nextPage(){
        if(index + 1 >= inventoryList.size()) return;
        index++;
    }

    public void prevPage(){
        if(index <= 0) return;
        index--;
    }
    @Override
    public Inventory getInventory() {
        return inventoryList.get(index);
    }

    public void click(HumanEntity player, int slot, boolean playerInventory) {
        ItemStack itemStack = getInventory().getItem(slot);
        ItemStack playerItemStack = player.getInventory().getItem(slot);

        BoxType boxType = plugin.getManager().getBoxType(type);

        switch (slot){
            case 0 -> prevPage();
            case 45 -> nextPage();
        }

        ((Player)player).playSound(player, Sound.UI_BUTTON_CLICK, 0.5f, 1f);

        if(!playerInventory && emptySlots.contains(slot) && (itemStack != null && !itemStack.getType().equals(Material.AIR))){
            ItemMeta meta = itemStack.getItemMeta();
            if(meta == null) return;

            int id = Objects.requireNonNull(meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "id"), PersistentDataType.INTEGER));
            EditItemInventory editItemInventory = new EditItemInventory(plugin, type, boxType.getItem(id));
            plugin.getManager().editItemInventoryMap.put((Player) player, editItemInventory);
            player.openInventory(editItemInventory.getInventory());
        }

        if(playerInventory && playerItemStack != null){
            boxType.addAndSave(new BoxItem(plugin, 50, boxType.getDefaultItemColor(), playerItemStack.clone()));
            boxType.updateEditInventory();
        }
    }

    public void updateItem(BoxItem boxItem){
        Integer[] assigned = assigned_items.get(boxItem.getID());
        if(assigned == null) return;

        Inventory inventory = inventoryList.get(assigned[0]);
        int slot = assigned[1];
        inventory.setItem(slot, boxItem.getIconItemStack());
    }

    private static class ItemComparator implements Comparator<BoxItem>{
        @Override
        public int compare(BoxItem o1, BoxItem o2) {
            return o1.getID() - o2.getID();
        }
    }
}
