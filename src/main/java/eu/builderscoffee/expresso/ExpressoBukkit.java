package eu.builderscoffee.expresso;


import eu.builderscoffee.api.bukkit.BuildersCoffeeAPI;
import eu.builderscoffee.api.bukkit.gui.InventoryManager;
import eu.builderscoffee.api.bukkit.utils.Plugins;
import eu.builderscoffee.api.common.data.tables.Profil;
import eu.builderscoffee.api.common.events.EventHandler;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.commons.common.redisson.topics.CommonTopics;
import eu.builderscoffee.expresso.buildbattle.BuildBattle;
import eu.builderscoffee.expresso.buildbattle.tasks.BoardTask;
import eu.builderscoffee.expresso.commands.JuryCommand;
import eu.builderscoffee.expresso.commands.PlotCommand;
import eu.builderscoffee.expresso.commands.TeamCommand;
import eu.builderscoffee.expresso.configuration.CacheConfiguration;
import eu.builderscoffee.expresso.configuration.MessageConfiguration;
import eu.builderscoffee.expresso.configuration.SettingsConfiguration;
import eu.builderscoffee.expresso.listeners.bukkit.CompetitorListener;
import eu.builderscoffee.expresso.listeners.bukkit.PlayerListener;
import eu.builderscoffee.expresso.listeners.bukkit.PlotListener;
import eu.builderscoffee.expresso.listeners.bukkit.TeamListeners;
import eu.builderscoffee.expresso.listeners.redisson.ConfigListener;
import eu.builderscoffee.expresso.listeners.redisson.HeartBeatListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static eu.builderscoffee.api.common.configuration.Configuration.readOrCreateConfiguration;

public class ExpressoBukkit extends JavaPlugin {

    // Instance
    @Getter
    private static ExpressoBukkit instance;
    @Getter
    private static BuildBattle buildBattle;

    // Configuration
    @Getter
    public static Map<Profil.Languages, MessageConfiguration> messages;
    @Getter
    public static SettingsConfiguration settings;
    @Getter
    public static CacheConfiguration cache;

    // Manager
    @Getter
    public static InventoryManager inventoryManager;
    @Getter
    private static ExecutionManager executionManager;

    @Override
    public void onEnable() {
        // Instance
        instance = this;
        buildBattle = new BuildBattle();

        // Read or create configurations
        messages = readOrCreateConfiguration(this.getName(), MessageConfiguration.class, Profil.Languages.class);
        settings = readOrCreateConfiguration(this.getName(), SettingsConfiguration.class);
        cache = readOrCreateConfiguration(this.getName(), CacheConfiguration.class);

        // Manager
        executionManager = new ExecutionManager();
        inventoryManager = BuildersCoffeeAPI.getInvManager();

        // Start Manager
        executionManager.start();

        // Register Bukkit Listeners
        Plugins.registerListeners(this, new PlayerListener(), new CompetitorListener(), new TeamListeners(), new PlotListener());

        // Register Redis Listeners
        Redis.subscribe(CommonTopics.SERVER_MANAGER, new ConfigListener());

        // Register BuildCoffee Event Listeners
        EventHandler.getInstance().addListener(new HeartBeatListener());

        // Register Command Executors
        this.getCommand("jury").setExecutor(new JuryCommand());
        this.getCommand("group").setExecutor(new TeamCommand());
        this.getCommand("eplot").setExecutor(new PlotCommand());

    }

    @Override
    public void onDisable() {
        // Stop custom tasks
        executionManager.shutdown();
    }

}
