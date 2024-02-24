package net.kokoricraft.giftbox.commands;

import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.objects.BoxType;
import net.kokoricraft.giftbox.objects.NekoItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
            plugin.getUtils().sendMessage(sender, "&cUse /"+label+" help");
            return true;
        }

        switch (arguments[0].toLowerCase()){
            case "create" -> createCommand(sender, label, arguments);
            case "edit" -> editCommand(sender, label, arguments);
            case "give" -> giveCommand(sender, label, arguments);
            case "remove" -> removeCommand(sender, label, arguments);
            case "reload" -> reloadCommand(sender, label, arguments);
            case "test" -> testCommand(sender, label, arguments);
        }
        return true;
    }

    private void reloadCommand(CommandSender sender, String label, String[] arguments) {
        if(!sender.hasPermission("giftbox.command.remove")){
            plugin.getUtils().sendMessage(sender, "&cYou don't have permission to use this command.");
            return;
        }

        plugin.reloadConfig();
        plugin.getUtils().sendMessage(sender, "&aConfiguration reloaded successfully.");
    }

    private void removeCommand(CommandSender sender, String label, String[] arguments) {
        if(arguments.length != 2){
            plugin.getUtils().sendMessage(sender, "&cYou must use /"+label+" remove <box type>");
            return;
        }

        if(!sender.hasPermission("giftbox.command.remove")){
            plugin.getUtils().sendMessage(sender, "&cYou don't have permission to use this command.");
            return;
        }

        String name = arguments[1];

        BoxType boxType = plugin.getManager().getBoxType(name);

        if(boxType == null){
            plugin.getUtils().sendMessage(sender, "&cThis giftbox does not exist");
            return;
        }

        plugin.getManager().removeBox(name);
        plugin.getUtils().sendMessage(sender, "&cThe giftbox has been successfully deleted.");
    }

    private void giveCommand(CommandSender sender, String label, String[] arguments) {
        if (arguments.length < 2 || arguments.length > 4) {
            plugin.getUtils().sendMessage(sender, "&cUsage: /" + label + " give <box type> [player] [amount]");
            return;
        }

        String boxName = arguments[1];
        BoxType boxType = plugin.getManager().getBoxType(boxName);

        if (boxType == null) {
            plugin.getUtils().sendMessage(sender, "&cThat BoxGift does not exist!");
            return;
        }

        NekoItem nekoItem = boxType.getItem();

        if (nekoItem == null) {
            plugin.getUtils().sendMessage(sender, "&cThat BoxGift doesn't have item configuration");
            return;
        }

        int amount = 1;
        Player targetPlayer = null;

        if (arguments.length > 2) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arguments[2]);


            if (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline()) {
                if (offlinePlayer.isOnline()) {
                    targetPlayer = (Player) offlinePlayer;
                } else {
                    plugin.getUtils().sendMessage(sender, "&cThat player is not online.");
                    return;
                }
            } else {
                try {
                    amount = Integer.parseInt(arguments[2]);
                    if (amount <= 0) {
                        plugin.getUtils().sendMessage(sender, "&cAmount must be a positive integer.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    plugin.getUtils().sendMessage(sender, "&cInvalid amount specified.");
                    return;
                }
            }
        }

        if (arguments.length > 3) {
            try {
                amount = Integer.parseInt(arguments[3]);
                if (amount <= 0) {
                    plugin.getUtils().sendMessage(sender, "&cAmount must be a positive integer.");
                    return;
                }
            } catch (NumberFormatException e) {
                plugin.getUtils().sendMessage(sender, "&cInvalid amount specified.");
                return;
            }
        }

        if (!(sender instanceof Player)) {
            if (targetPlayer == null) {
                plugin.getUtils().sendMessage(sender, "&cYou must specify a player when executing this command from console.");
                return;
            }
        } else {
            if (!sender.hasPermission("giftbox.command.give.others") && targetPlayer != null && !sender.equals(targetPlayer)) {
                plugin.getUtils().sendMessage(sender, "&cYou don't have permission to give BoxGift to other players.");
                return;
            }
        }

        ItemStack itemStack = nekoItem.getItem().clone();
        itemStack.setAmount(amount);

        if (targetPlayer != null) {
            targetPlayer.getInventory().addItem(itemStack);
            plugin.getUtils().sendMessage(sender, "&aSuccessfully gave " + amount + " " + boxName + " BoxGift(s) to " + targetPlayer.getName());
        } else {
            ((Player) sender).getInventory().addItem(itemStack);
            plugin.getUtils().sendMessage(sender, "&aSuccessfully gave yourself " + amount + " " + boxName + " BoxGift(s).");
        }
    }


    private void createCommand(CommandSender sender, String label, String[] arguments){
        if(arguments.length != 2){
            plugin.getUtils().sendMessage(sender, "&cYou must use /"+label+" create <name>");
            return;
        }

        if(!(sender instanceof Player player)){
            plugin.getUtils().sendMessage(sender, "&cCommand only for players");
            return;
        }

        if(!sender.hasPermission("giftbox.command.create")){
            plugin.getUtils().sendMessage(sender, "&cYou don't have permission to use this command.");
            return;
        }

        String name = arguments[1];

        if(plugin.getManager().getBoxType(name) != null){
            plugin.getUtils().sendMessage(sender, "&cA giftbox with this name already exists \n use /"+label+" edit <name>");
            return;
        }

        BoxType boxType = new BoxType(plugin, name);
        plugin.getManager().addBox(name, boxType);
        plugin.getTypeConfigManager().createType(name);
        plugin.getManager().getBoxType(name).openEditInventory(player);
        plugin.getUtils().sendMessage(sender, "&aA giftbox has been created");
    }

    private void editCommand(CommandSender sender, String label, String[] arguments){
        if(arguments.length != 2){
            plugin.getUtils().sendMessage(sender, "&cYou must use /"+label+" edit <name>");
            return;
        }

        if(!(sender instanceof Player player)){
            plugin.getUtils().sendMessage(sender, "&cCommand only for players");
            return;
        }

        if(!sender.hasPermission("giftbox.command.edit")){
            plugin.getUtils().sendMessage(sender, "&cYou don't have permission to use this command.");
            return;
        }

        String name = arguments[1];
        BoxType boxType = plugin.getManager().getBoxType(name);
        if(boxType == null){
            plugin.getUtils().sendMessage(sender, "&cThere is no giftbox with this name \n use /"+label+" create <name>");
            return;
        }

        boxType.openEditInventory(player);
    }

    private void sendHelpMessage(CommandSender sender){

    }

    private void testCommand(CommandSender sender, String label, String[] arguments) {
        Player player = (Player)sender;
        for(int i = 0; i < 30; i++)
            plugin.getUtils().sendMessage(player, "&c");
    }
}
