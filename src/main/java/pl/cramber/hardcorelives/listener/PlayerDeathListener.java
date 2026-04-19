package pl.cramber.hardcorelives.listener;

import io.papermc.paper.ban.BanListType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.cramber.hardcorelives.Main;

import java.time.Duration;

//Needs refactore just as in PlayerInteractListener
public class PlayerDeathListener implements Listener {

    private final Main plugin;

    public PlayerDeathListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final int currentLives = this.plugin.getLivesRepository().getLives(player.getUniqueId());

        if (currentLives == -1) {
            final int startingLives = this.plugin.getConfig().getInt("starting_lives");
            this.plugin.getLivesRepository().setLives(player.getUniqueId(), startingLives);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getPlayer();
        int currentLives = this.plugin.getLivesRepository().getLives(player.getUniqueId());

        if (currentLives > 0) {
            currentLives--;
        }

        if (currentLives <= 0) {
            final int banHours = this.plugin.getConfig().getInt("ban_hours");
            final int livesAfterBan = this.plugin.getConfig().getInt("lives_after_ban");

            this.plugin.getLivesRepository().setLives(player.getUniqueId(), livesAfterBan);

            final String rawKickMessage = this.plugin.getConfig().getString("messages.kick_ban", "<red>Banned.</red>")
                    .replace("%hours%", String.valueOf(banHours));

            Bukkit.getBanList(BanListType.PROFILE).addBan(
                    player.getPlayerProfile(),
                    rawKickMessage,
                    Duration.ofHours(banHours),
                    "Main"
            );

            player.kick(MiniMessage.miniMessage().deserialize(rawKickMessage));
        } else {
            this.plugin.getLivesRepository().setLives(player.getUniqueId(), currentLives);

            final String rawDeathMessage = this.plugin.getConfig().getString("messages.death_lives_remaining", "<yellow>Lives: %lives%</yellow>")
                    .replace("%lives%", String.valueOf(currentLives));

            player.sendMessage(MiniMessage.miniMessage().deserialize(rawDeathMessage));
        }
    }
}