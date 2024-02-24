package net.kokoricraft.giftbox.commands;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandsCompleter implements TabCompleter {

    private final GiftBox plugin;

    public CommandsCompleter(GiftBox plugin) {
        this.plugin = plugin;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "edit", "reload", "give", "remove");
        }

        switch (args[0]) {
            case "edit", "remove", "give" -> {
                if (args.length == 2) {
                    return new ArrayList<>(plugin.getManager().getBoxesNames());
                } else if (args.length == 3 && args[0].equals("give")) {
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .collect(Collectors.toList());
                }
            }
        }

        return new ArrayList<>();
    }
}
