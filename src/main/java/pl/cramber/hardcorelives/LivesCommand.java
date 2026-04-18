package pl.cramber.hardcorelives;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LivesCommand implements CommandExecutor {
    private final HardcoreLives plugin;

    public LivesCommand(HardcoreLives plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // maybe you can use switch for this
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                return true;
            }
            int lives = plugin.getDataManager().getLives(player.getUniqueId());
            String msg = plugin.getConfig().getString("messages.command_lives_self", "<green>You have %lives% lives remaining.</green>")
                    .replace("%lives%", String.valueOf(lives == -1 ? plugin.getConfig().getInt("starting_lives") : lives));
            player.sendMessage(MiniMessage.miniMessage().deserialize(msg));
            return true;
        }

        if (args.length == 1) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            int lives = plugin.getDataManager().getLives(target.getUniqueId());
            if (lives == -1) {
                String msg = plugin.getConfig().getString("messages.command_player_not_found", "<red>Player not found.</red>");
                sender.sendMessage(MiniMessage.miniMessage().deserialize(msg));
                return true;
            }
            String msg = plugin.getConfig().getString("messages.command_lives_other", "<green>%player% has %lives% lives remaining.</green>")
                    .replace("%player%", target.getName() != null ? target.getName() : args[0])
                    .replace("%lives%", String.valueOf(lives));
            sender.sendMessage(MiniMessage.miniMessage().deserialize(msg));
            return true;
        }

        if (args.length == 3) {
            if (!sender.hasPermission("hardcorelives.admin")) {
                String msg = plugin.getConfig().getString("messages.no_permission", "<red>No permission (hardcorelives.admin).</red>");
                sender.sendMessage(MiniMessage.miniMessage().deserialize(msg));
                return true;
            }

            String subCmd = args[0].toLowerCase();
            //and for this
            if (subCmd.equals("set") || subCmd.equals("add")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    String msg = plugin.getConfig().getString("messages.invalid_number", "<red>Invalid number provided.</red>");
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(msg));
                    return true;
                }

                int current = plugin.getDataManager().getLives(target.getUniqueId());
                if (current == -1) {
                    current = plugin.getConfig().getInt("starting_lives");
                }

                int newLives = subCmd.equals("set") ? amount : current + amount;
                int maxLives = plugin.getConfig().getInt("max_lives");

                if (newLives > maxLives) {
                    newLives = maxLives;
                }

                plugin.getDataManager().setLives(target.getUniqueId(), newLives);

                String msgKey = subCmd.equals("set") ? "messages.admin_set" : "messages.admin_add";
                String msg = plugin.getConfig().getString(msgKey, "<green>Updated lives.</green>")
                        .replace("%player%", target.getName() != null ? target.getName() : args[1])
                        .replace("%lives%", String.valueOf(newLives));

                sender.sendMessage(MiniMessage.miniMessage().deserialize(msg));
                return true;
            }
        }

        String msg = plugin.getConfig().getString("messages.invalid_usage", "<red>Usage: /lives [player] | /lives <set/add> <player> <amount></red>");
        sender.sendMessage(MiniMessage.miniMessage().deserialize(msg));
        return true;
    }
}