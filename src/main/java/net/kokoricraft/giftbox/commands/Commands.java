package net.kokoricraft.giftbox.commands;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxSkins;
import net.kokoricraft.giftbox.objects.Box;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class Commands implements CommandExecutor {

    private final GiftBox plugin;

    public Commands(GiftBox plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        Location location = player.getLocation();
        Box box = new Box("test", location, plugin);
        box.setSkin(BoxSkins.DEFAULT);
        BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
        BlockFace face = faces[new Random().nextInt(faces.length-1)];
        box.place(face);

        player.sendMessage("placed test gift box "+face);
        return true;
    }
}
