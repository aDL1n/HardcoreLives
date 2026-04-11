package pl.cramber.hardcorelives;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

public class PlayerInteractListener implements Listener {
    private final HardcoreLives plugin;

    public PlayerInteractListener(HardcoreLives plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (!plugin.getConfig().getBoolean("life_item.enabled")) {
            return;
        }

        Material material = Material.matchMaterial(plugin.getConfig().getString("life_item.material", "NETHER_STAR"));
        if (item.getType() != material) {
            return;
        }

        boolean isLifeItem = false;

        if (plugin.getConfig().contains("life_item.custom_model_data")) {
            int requiredCmd = plugin.getConfig().getInt("life_item.custom_model_data");

            if (item.hasItemMeta() && item.getItemMeta().hasCustomModelDataComponent()) {
                CustomModelDataComponent cmdComponent = item.getItemMeta().getCustomModelDataComponent();
                if (!cmdComponent.getFloats().isEmpty()) {
                    if (cmdComponent.getFloats().getFirst() == (float) requiredCmd) {
                        isLifeItem = true;
                    }
                }
            }
        } else {
            isLifeItem = true;
        }

        if (isLifeItem) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            int lives = plugin.getDataManager().getLives(player.getUniqueId());
            if (lives == -1) {
                lives = plugin.getConfig().getInt("starting_lives");
            }

            int maxLives = plugin.getConfig().getInt("max_lives");
            if (lives >= maxLives) {
                String msg = plugin.getConfig().getString("messages.max_lives_reached", "<red>You cannot have more than %max% lives!</red>")
                        .replace("%max%", String.valueOf(maxLives));
                player.sendMessage(MiniMessage.miniMessage().deserialize(msg));
                return;
            }

            plugin.getDataManager().setLives(player.getUniqueId(), lives + 1);
            item.subtract(1);

            String msg = plugin.getConfig().getString("messages.item_used", "<green>Added 1 life! You now have %lives% lives.</green>")
                    .replace("%lives%", String.valueOf(lives + 1));
            player.sendMessage(MiniMessage.miniMessage().deserialize(msg));
        }
    }
}