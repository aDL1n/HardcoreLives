package pl.cramber.hardcorelives;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class LivesExpansion extends PlaceholderExpansion {

    private final HardcoreLives plugin;

    public LivesExpansion(HardcoreLives plugin) {
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
        return plugin.getPluginMeta().getAuthors().isEmpty() ? "Cramber" : plugin.getPluginMeta().getAuthors().getFirst();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        if (params.equalsIgnoreCase("lives")) {
            int lives = plugin.getDataManager().getLives(player.getUniqueId());
            return lives == -1 ? "0" : String.valueOf(lives);
        }

        return null;
    }
}