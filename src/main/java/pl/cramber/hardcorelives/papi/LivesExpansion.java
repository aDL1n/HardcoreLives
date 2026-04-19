package pl.cramber.hardcorelives.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import pl.cramber.hardcorelives.Main;

import java.util.List;

public class LivesExpansion extends PlaceholderExpansion {

    private final Main plugin;

    public LivesExpansion(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "hardcorelives";
    }

    @Override
    public @NotNull String getAuthor() {
        final List<String> authors = this.plugin.getPluginMeta().getAuthors();
        return authors.isEmpty() ? "Cramber" : authors.getFirst();
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getPluginMeta().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        if (params.equalsIgnoreCase("lives")) {
            final int lives = plugin.getLivesRepository().getLives(player.getUniqueId());
            return lives == -1 ? "0" : String.valueOf(lives);
        }

        return null;
    }
}