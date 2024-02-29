package net.kokoricraft.giftbox;

import net.kokoricraft.giftbox.commands.Commands;
import net.kokoricraft.giftbox.commands.CommandsCompleter;
import net.kokoricraft.giftbox.listeners.PlayerListeners;
import net.kokoricraft.giftbox.managers.*;
import net.kokoricraft.giftbox.utils.Metrics;
import net.kokoricraft.giftbox.sql.MySQLConnector;
import net.kokoricraft.giftbox.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class GiftBox extends JavaPlugin {

    private Utils utils;
    private SkinsConfigManager skinsConfigManager;
    private Manager manager;
    private AnimationManager animationManager;
    private TypeConfigManager typeConfigManager;
    private MySQLConnector sqlConnector;

    @Override
    public void onEnable() {
        // Plugin startup logic
        initClass();
        initCommands();
        initListeners();
    }

    @Override
    public void onDisable() {
    }

    private void initClass(){
        new DefaultGenerator(this).check();

        utils = new Utils(this);
        skinsConfigManager = new SkinsConfigManager(this);
        skinsConfigManager.loadSkins();
        manager = new Manager(this);
        animationManager = new AnimationManager(this);
        animationManager.initAnimations();
        typeConfigManager = new TypeConfigManager(this);
        typeConfigManager.loadTypes();
        typeConfigManager.loadItems();
        new Metrics(this, 21124);
    }

    private void initCommands(){
        PluginCommand command = getCommand("giftbox");
        if(command == null) return;

        command.setExecutor(new Commands(this));
        command.setTabCompleter(new CommandsCompleter(this));
    }

    private void initListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerListeners(this), this);
    }

    public void reloadConfig(){
        new DefaultGenerator(this).check();

        skinsConfigManager = new SkinsConfigManager(this);
        skinsConfigManager.loadSkins();
        typeConfigManager = new TypeConfigManager(this);
        typeConfigManager.loadTypes();
        typeConfigManager.loadItems();
    }

    public Utils getUtils(){
        return utils;
    }

    public Manager getManager(){
        return manager;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public TypeConfigManager getTypeConfigManager() {
        return typeConfigManager;
    }

    public SkinsConfigManager getSkinsConfigManager() {
        return skinsConfigManager;
    }
}
