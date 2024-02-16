package net.kokoricraft.giftbox;

import net.kokoricraft.giftbox.commands.Commands;
import net.kokoricraft.giftbox.listeners.PlayerListeners;
import net.kokoricraft.giftbox.managers.AnimationManager;
import net.kokoricraft.giftbox.managers.Manager;
import net.kokoricraft.giftbox.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class GiftBox extends JavaPlugin {

    private Utils utils;
    private Manager manager;
    private AnimationManager animationManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        initClass();
        initCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initClass(){
        this.utils = new Utils(this);
        this.manager = new Manager(this);
        this.animationManager = new AnimationManager(this);
        animationManager.initAnimations();
        initListeners();
    }

    private void initCommands(){
        Objects.requireNonNull(getCommand("giftbox")).setExecutor(new Commands(this));
    }

    private void initListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerListeners(this), this);
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
}
