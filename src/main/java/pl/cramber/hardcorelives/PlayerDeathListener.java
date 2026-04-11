package pl.cramber.hardcorelives;

import io.papermc.paper.ban.BanListType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;

public class PlayerDeathListener implements Listener {

    private final HardcoreLives plugin;

    public PlayerDeathListener(HardcoreLives plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int currentLives = plugin.getDataManager().getLives(player.getUniqueId());

        if (currentLives == -1) {
            int startingLives = plugin.getConfig().getInt("starting_lives");
            plugin.getDataManager().setLives(player.getUniqueId(), startingLives);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        int currentLives = plugin.getDataManager().getLives(player.getUniqueId());

        if (currentLives > 0) {
            currentLives--;
        }

        if (currentLives <= 0) {
            int banHours = plugin.getConfig().getInt("ban_hours");
            int livesAfterBan = plugin.getConfig().getInt("lives_after_ban");

            plugin.getDataManager().setLives(player.getUniqueId(), livesAfterBan);

            String rawKickMessage = plugin.getConfig().getString("messages.kick_ban", "<red>Banned.</red>")
                    .replace("%hours%", String.valueOf(banHours));

            Bukkit.getBanList(BanListType.PROFILE).addBan(
                    player.getPlayerProfile(),
                    rawKickMessage,
                    Duration.ofHours(banHours),
                    "HardcoreLives"
            );

            player.kick(MiniMessage.miniMessage().deserialize(rawKickMessage));
        } else {
            plugin.getDataManager().setLives(player.getUniqueId(), currentLives);

            String rawDeathMessage = plugin.getConfig().getString("messages.death_lives_remaining", "<yellow>Lives: %lives%</yellow>")
                    .replace("%lives%", String.valueOf(currentLives));

            player.sendMessage(MiniMessage.miniMessage().deserialize(rawDeathMessage));
        }
    }
}