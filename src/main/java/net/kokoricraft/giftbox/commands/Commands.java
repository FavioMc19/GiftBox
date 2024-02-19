package net.kokoricraft.giftbox.commands;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;

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
            case "test" -> testCommand(sender, label, arguments);
        }
        return true;
    }

    private void testCommand(CommandSender sender, String label, String[] arguments) {
        Player player = (Player)sender;
        TextDisplay display = player.getWorld().spawn(player.getLocation(), TextDisplay.class);
        display.setText("Test awa a");
        display.setBillboard(Display.Billboard.CENTER);
        Transformation transformation = display.getTransformation();
        transformation.getTranslation().set(1.3, 0.3, 0);
        display.setTransformation(transformation);

        player.addPassenger(display);
        player.sendMessage("spawned");


        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                display.setRotation(player.getLocation().getYaw(), 0);
            }
        }, 0, 0);

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                task.cancel();
                display.remove();
                player.sendMessage("removed");
            }
        }, 20 * 20);
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
