package net.kokoricraft.giftbox.listeners;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.Box;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.List;

public class WorldListeners implements Listener {
    private final GiftBox plugin;

    public WorldListeners(GiftBox plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        List<Box> placed = plugin.getManager().placed_boxes;
        if(placed.isEmpty()) return;

        for(Box box : placed){
            if(event.getChunk().equals(box.getLocation().getChunk())){
                box.forceDropItem();
                box.remove();
            }
        }
    }
}
