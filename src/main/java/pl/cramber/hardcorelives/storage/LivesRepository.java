package pl.cramber.hardcorelives.storage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LivesRepository implements Listener {
    private final LivesLoader loader;
    private final Map<UUID, Integer> lives = new HashMap<>();
    private final int defaultLives;

    public LivesRepository(LivesLoader loader, int defaultLives) {
        this.loader = loader;
        this.defaultLives = defaultLives;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {
        loadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerQuit(PlayerQuitEvent event) {
        unloadPlayer(event.getPlayer().getUniqueId());
    }

    public void loadPlayer(UUID uuid) {
        final int playerLives = loader.load(uuid, this.defaultLives);
        this.lives.put(uuid, playerLives);
    }

    public void unloadPlayer(UUID uuid) {
        savePlayer(uuid);
        this.lives.remove(uuid);
    }

    public int getLives(UUID uuid) {
        return this.lives.getOrDefault(uuid, this.defaultLives);
    }

    public void setLives(UUID uuid, int amount) {
        this.lives.put(uuid, Math.max(0, amount));
    }

    public void savePlayer(UUID uuid) {
        if (this.lives.containsKey(uuid)) {
            this.loader.save(uuid, this.lives.get(uuid));
        }
    }

}
