package net.kokoricraft.giftbox.commands;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.enums.BoxSkins;
import net.kokoricraft.giftbox.guis.EditInventory;
import net.kokoricraft.giftbox.guis.EditItemInventory;
import net.kokoricraft.giftbox.objects.Box;
import net.kokoricraft.giftbox.objects.BoxType;
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if(arguments.length == 0){
            plugin.getUtils().sendMessage(sender, "&cUsa /"+label+" help");
            return true;
        }

        switch (arguments[0].toLowerCase()){
            case "create" -> createCommand(sender, label, arguments);
            case "edit" -> editCommand(sender, label, arguments);
        }
        return true;
    }

    private void createCommand(CommandSender sender, String label, String[] arguments){
        if(arguments.length != 2){
            plugin.getUtils().sendMessage(sender, "&cDebes usar /"+label+" create <nombre>");
            return;
        }

        if(!(sender instanceof Player player)){
            plugin.getUtils().sendMessage(sender, "&cComando solo para jugadores");
            return;
        }

        String name = arguments[1];
        plugin.getManager().getBoxType(name).openEditInventory(player);
    }

    private void editCommand(CommandSender sender, String label, String[] arguments){

    }

    private void sendHelpMessage(CommandSender sender){

    }
}
