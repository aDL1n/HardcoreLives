package pl.cramber.hardcorelives;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class HardcoreLives extends JavaPlugin {

    private DataManager dataManager;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        dataManager = new DataManager(this);

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);

        PluginCommand livesCommand = getCommand("lives");
        if (livesCommand != null) {
            livesCommand.setExecutor(new LivesCommand(this));
        } else {
            getLogger().severe("The /lives command is not registered in plugin.yml!");
        }

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

    public DataManager getDataManager() {
        return dataManager;
    }
}