package net.kokoricraft.giftbox;

import net.kokoricraft.giftbox.commands.Commands;
import net.kokoricraft.giftbox.listeners.PlayerListeners;
import net.kokoricraft.giftbox.managers.AnimationManager;
import net.kokoricraft.giftbox.managers.Manager;
import net.kokoricraft.giftbox.managers.SkinsConfigManager;
import net.kokoricraft.giftbox.managers.TypeConfigManager;
import net.kokoricraft.giftbox.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class GiftBox extends JavaPlugin {

    private Utils utils;
    private SkinsConfigManager skinsConfigManager;
    private Manager manager;
    private AnimationManager animationManager;
    private TypeConfigManager typeConfigManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        initClass();
        initCommands();
        initListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initClass(){
        utils = new Utils(this);
        skinsConfigManager = new SkinsConfigManager(this);
        skinsConfigManager.loadSkins();
        manager = new Manager(this);
        animationManager = new AnimationManager(this);
        animationManager.initAnimations();
        typeConfigManager = new TypeConfigManager(this);
        typeConfigManager.loadTypes();
        typeConfigManager.loadItems();
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

    public TypeConfigManager getTypeConfigManager() {
        return typeConfigManager;
    }

    public SkinsConfigManager getSkinsConfigManager() {
        return skinsConfigManager;
    }
}
