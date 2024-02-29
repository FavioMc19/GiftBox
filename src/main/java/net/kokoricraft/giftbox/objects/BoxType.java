package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.guis.EditInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class BoxType {
    private final GiftBox plugin;
    private final String name;
    private final Map<Integer, BoxItem> items = new HashMap<>();
    private final Random random;
    private final EditInventory editInventory;
    private int idCounter;
    private NekoItem item;
    private String skin;
    private String defaultItemColor;
    private boolean permissions_enabled = false;
    private String permissions_permission;
    private String animation = "default_animation";

    public BoxType(GiftBox plugin, String name){
        this.plugin = plugin;
        this.name = name;
        this.random = new Random();
        this.editInventory = new EditInventory(plugin, name);
        permissions_permission = "giftbox.box.use."+name;
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

    public void update(BoxItem item){
        plugin.getTypeConfigManager().updateItem(name, item);
    }

    public void removeAndSave(int id){
        this.items.remove(id);
        plugin.getTypeConfigManager().removeItem(name, id);
        updateEditInventory();
    }

    public int generateID(){
        idCounter++;
        NekoConfig config = plugin.getTypeConfigManager().items_config.get(name);
        if(config == null){
            config = new NekoConfig("items/"+name+".yml", plugin);
            plugin.getTypeConfigManager().items_config.put(name, config);
        }

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
        if(boxItems.isEmpty()) return null;

        return boxItems.get(boxItems.size() - 1);
    }

    public void updateEditInventory(){
        editInventory.setItems();
    }

    public void openEditInventory(Player player){
        player.openInventory(editInventory.getInventory());
    }

    public BoxItem getItem(int id) {
        return items.get(id);
    }

    public void setIDCounter(int idCounter){
        this.idCounter = idCounter;
    }

    public EditInventory getInventory() {
        return editInventory;
    }

    public void setItem(NekoItem item) {
        this.item = item;
    }

    public NekoItem getItem(){
        return item;
    }

    public void setSkin(String skin) {
        if(skin == null) {
            this.skin = "normal";
            return;
        }

        this.skin = skin;
    }

    public String getSkin(){
        return skin;
    }

    public void setDefaultItemColor(String defaultItemColor) {
        this.defaultItemColor = defaultItemColor;
    }

    public String getDefaultItemColor(){
        return defaultItemColor;
    }

    public void setPermissions(ConfigurationSection permissions) {
        permissions_enabled = permissions.getBoolean("enabled");

        if(permissions.contains("permission"))
            permissions_permission = permissions.getString("permission");
    }

    public boolean isNeedPermission(){
        return permissions_enabled;
    }

    public String getPermission(){
        return permissions_permission;
    }

    public void setAnimation(String animation){
        this.animation = animation;
    }

    public String getAnimation(){
        return animation;
    }
}
