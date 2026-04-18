package pl.cramber.hardcorelives;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class HardcoreLives extends JavaPlugin {

    private DataManager dataManager;

    private final static int PLUGIN_ID = 30798;

    @Override
    public void onEnable() {
        super.saveDefaultConfig();

        this.dataManager = new DataManager(this);

        // bStats
        new Metrics(this, PLUGIN_ID);

        registerEvents();

        getCommand("lives").setExecutor(new LivesCommand(this));

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new LivesExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.save();
        }
    }

    private void registerEvents() {
        registerEventListener(new PlayerDeathListener(this));
        registerEventListener(new PlayerInteractListener(this));
    }

    private void registerEventListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}