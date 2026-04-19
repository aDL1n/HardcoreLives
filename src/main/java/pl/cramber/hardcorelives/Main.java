package pl.cramber.hardcorelives;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.cramber.hardcorelives.command.LivesCommand;
import pl.cramber.hardcorelives.listener.PlayerDeathListener;
import pl.cramber.hardcorelives.listener.PlayerInteractListener;
import pl.cramber.hardcorelives.papi.LivesExpansion;
import pl.cramber.hardcorelives.storage.LivesLoader;
import pl.cramber.hardcorelives.storage.LivesRepository;

public final class Main extends JavaPlugin {

    private final static int PLUGIN_ID = 30798;

    private LivesRepository livesRepository;

    @Override
    public void onEnable() {
        super.saveDefaultConfig();

        initializeRepository();

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
        if (this.livesRepository != null) {
            Bukkit.getOnlinePlayers()
                    .forEach(player -> this.livesRepository.unloadPlayer(player.getUniqueId()));
        }
    }

    private void initializeRepository() {
        LivesLoader loader = new LivesLoader(getDataFolder());

        final int defaultLives = getConfig().getInt("starting_lives", 3);
        this.livesRepository = new LivesRepository(loader, defaultLives);
        registerEventListener(livesRepository);
    }

    private void registerEvents() {
        registerEventListener(new PlayerDeathListener(this));
        registerEventListener(new PlayerInteractListener(this));
    }

    private void registerEventListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    // :(
    public LivesRepository getLivesRepository() {
        return livesRepository;
    }
}