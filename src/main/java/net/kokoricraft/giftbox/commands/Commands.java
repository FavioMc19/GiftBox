package net.kokoricraft.giftbox.commands;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.BoxType;
import net.kokoricraft.giftbox.objects.NekoItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

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
            case "give" -> giveCommand(sender, label, arguments);
            case "test" -> testCommand(sender, label, arguments);
        }
        return true;
    }

    private void giveCommand(CommandSender sender, String label, String[] arguments) {
        if(arguments.length != 2){
            plugin.getUtils().sendMessage(sender, "&cDebes usar /"+label+" give <box type>");
            return;
        }

        if(!(sender instanceof Player player)){
            plugin.getUtils().sendMessage(sender, "&cComando solo para jugadores");
            return;
        }

        String name = arguments[1];

        BoxType boxType = plugin.getManager().getBoxType(name);

        if(boxType == null){
            plugin.getUtils().sendMessage(sender, "&cEsa BoxGift no existe!");
            return;
        }

        NekoItem nekoItem = boxType.getItem();

        if(nekoItem == null){
            plugin.getUtils().sendMessage(sender, "&cEsa BoxGift no tiene configuracion de item");
            return;
        }

        ItemStack itemStack = nekoItem.getItem();
        player.getInventory().addItem(itemStack);
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

        if(plugin.getManager().getBoxType(name) != null){
            plugin.getUtils().sendMessage(sender, "&cYa existe una giftbox con este nombre \n usa /"+label+" edit <nombre>");
            return;
        }

        BoxType boxType = new BoxType(plugin, name);
        plugin.getManager().addBox(name, boxType);
        plugin.getTypeConfigManager().createType(name);
        plugin.getManager().getBoxType(name).openEditInventory(player);
        plugin.getUtils().sendMessage(sender, "&aCreado awa");
    }

    private void editCommand(CommandSender sender, String label, String[] arguments){
        if(arguments.length != 2){
            plugin.getUtils().sendMessage(sender, "&cDebes usar /"+label+" edit <nombre>");
            return;
        }

        if(!(sender instanceof Player player)){
            plugin.getUtils().sendMessage(sender, "&cComando solo para jugadores");
            return;
        }

        String name = arguments[1];
        BoxType boxType = plugin.getManager().getBoxType(name);
        if(boxType == null){
            plugin.getUtils().sendMessage(sender, "&cNo existe una giftbox con este nombre \n usa /"+label+" create <nombre>");
            return;
        }

        boxType.openEditInventory(player);
    }

    private void sendHelpMessage(CommandSender sender){

    }

    private void testCommand(CommandSender sender, String label, String[] arguments) {
        Player player = (Player)sender;
        File file = new File(plugin.getDataFolder()+"/skins/normal/body.png");
        player.sendMessage(plugin.getUtils().getImageHash(file));
    }
}
