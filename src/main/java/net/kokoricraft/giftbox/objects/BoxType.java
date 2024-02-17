package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;

import java.util.*;

public class BoxType {
    private final GiftBox plugin;
    private final String name;
    private final Map<UUID, BoxItem> items = new HashMap<>();
    private final Random random;

    public BoxType(GiftBox plugin, String name){
        this.plugin = plugin;
        this.name = name;
        this.random = new Random();
    }

    public String getName(){
        return name;
    }

    public Collection<BoxItem> getItems(){
        return items.values();
    }

    public void addItem(BoxItem item){
        items.put(item.getUUID(), item);
    }

    public boolean contains(UUID uuid){
        return items.containsKey(uuid);
    }

    public void addAndSave(BoxItem item){
        items.put(item.getUUID(), item);
        plugin.getTypeConfigManager().addItem(name, item);
    }

    public void removeAndSave(UUID uuid){
        this.items.remove(uuid);
        plugin.getTypeConfigManager().removeItem(name, uuid);
    }

    public UUID generateUUID(){
        UUID uuid;
       do {
           uuid = UUID.randomUUID();
       } while (contains(uuid));

       return uuid;
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
}
