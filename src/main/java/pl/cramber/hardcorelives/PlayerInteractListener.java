package pl.cramber.hardcorelives;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class PlayerInteractListener implements Listener {
    private final HardcoreLives plugin;

    public PlayerInteractListener(HardcoreLives plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!this.plugin.getConfig().getBoolean("life_item.enabled")) return;

        final Action eventAction = event.getAction();
        if (eventAction != Action.RIGHT_CLICK_AIR && eventAction != Action.RIGHT_CLICK_BLOCK) return;

        final ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) return;

        //bad realisation bacause you got material from config every call of this method
        final Material material = getMeterialFromConfig("life_item.material", Material.NETHER_STAR);
        if (item.getType() != material) return;

        if (!isLifeItem(item)) return;
        event.setCancelled(true);

        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        int currentLives = this.plugin.getDataManager().getLives(playerUUID);
        if (currentLives == -1) {
            currentLives = this.plugin.getConfig().getInt("starting_lives");
        }

        final int maxLives = this.plugin.getConfig().getInt("max_lives");
        if (currentLives >= maxLives) {
            //Try use MiniMessage placeholders
            String msg = this.plugin.getConfig().getString("messages.max_lives_reached", "<red>You cannot have more than %max% lives!</red>")
                    .replace("%max%", String.valueOf(maxLives));
            player.sendMessage(MiniMessage.miniMessage().deserialize(msg));
            return;
        }

        this.plugin.getDataManager().setLives(player.getUniqueId(), currentLives + 1);
        item.subtract(1);

        String msg = this.plugin.getConfig().getString("messages.item_used", "<green>Added 1 life! You now have %lives% lives.</green>")
                .replace("%lives%", String.valueOf(currentLives + 1));
        player.sendMessage(MiniMessage.miniMessage().deserialize(msg));
    }

    private @NotNull Material getMeterialFromConfig(final @NotNull String pathToMaterial, final @NotNull Material defaultMaterial) {
        final String materialName = this.plugin.getConfig().getString(pathToMaterial);
        if (materialName == null) return defaultMaterial;

        final Material gotMaterial = Material.matchMaterial(pathToMaterial);
        return gotMaterial != null ? gotMaterial : defaultMaterial;
    }

    private boolean isLifeItem(final @NotNull ItemStack item) {
        if (!this.plugin.getConfig().contains("life_item.custom_model_data")) return true;
        final int customModelData = this.plugin.getConfig().getInt("life_item.custom_model_data");

        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null && !itemMeta.hasCustomModelDataComponent()) return false;

        final List<Float> customModelDataFloats = item.getItemMeta().getCustomModelDataComponent().getFloats();

        return (!customModelDataFloats.isEmpty() && customModelDataFloats.getFirst() == customModelData)
                ? true : false;
    }
}