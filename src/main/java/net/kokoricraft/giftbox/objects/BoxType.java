package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.guis.EditInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BoxType {
    private final GiftBox plugin;
    private final String name;
    private final Map<Integer, BoxItem> items = new HashMap<>();
    private final Random random;
    private final EditInventory editInventory;
    private int idCounter;

    public BoxType(GiftBox plugin, String name){
        this.plugin = plugin;
        this.name = name;
        this.random = new Random();
        this.editInventory = new EditInventory(plugin, name);
    }

    public String getName(){
        return name;
    }

    public List<BoxItem> getItems(){
        return new ArrayList<>(this.items.values());
    }

    public void addItem(BoxItem item){
        items.put(item.getID(), item);
    }

    public boolean contains(int id){
        return items.containsKey(id);
    }

    public void addAndSave(BoxItem item){
        item.setID(generateID());
        items.put(item.getID(), item);
        plugin.getTypeConfigManager().addItem(name, item);
    }

    public void removeAndSave(int id){
        this.items.remove(id);
        plugin.getTypeConfigManager().removeItem(name, id);
    }

    public int generateID(){
        idCounter++;
        Bukkit.broadcastMessage("get config "+name);
        NekoConfig config = plugin.getTypeConfigManager().items_config.get(name);
        config.set("id-counter", idCounter);
        return idCounter;
    }

    public BoxItem selectRandomItem() {
        List<BoxItem> boxItems = new ArrayList<>(items.values());
        double totalChance = 0;
        for (BoxItem item : boxItems) {
            totalChance += item.getChance();
        }

        double randomNumber = random.nextDouble() * totalChance;

        double cumulativeProbability = 0;
        for (BoxItem item : boxItems) {
            cumulativeProbability += item.getChance();
            if (randomNumber <= cumulativeProbability) {
                return item;
            }
        }

        return boxItems.get(boxItems.size() - 1);
    }

    public void updateEditInventory(){
        editInventory.setItems();
    }

    public void openEditInventory(Player player){
        Bukkit.broadcastMessage("aaaaa");
        player.openInventory(editInventory.getInventory());
    }

    public BoxItem getItem(int id) {
        return items.get(id);
    }

    public void setIDCounter(int idCounter){
        this.idCounter = idCounter;
    }
}
