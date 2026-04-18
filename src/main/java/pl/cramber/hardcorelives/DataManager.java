package pl.cramber.hardcorelives;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

//God class, fix this
public class DataManager {
    private final HardcoreLives plugin;
    private File dataFile;
    private FileConfiguration dataConfig;

    public DataManager(HardcoreLives plugin) {
        this.plugin = plugin;
        setup();
    }

    public void setup() {
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            try {
                if (!dataFile.createNewFile()) {
                    plugin.getLogger().warning("Could not create data.yml because it already exists.");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create data.yml file!", e);
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public int getLives(UUID uuid) {
        return dataConfig.getInt(uuid.toString(), -1);
    }

    public void setLives(UUID uuid, int lives) {
        dataConfig.set(uuid.toString(), lives);
        save();
    }

    public void save() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save data.yml file!", e);
        }
    }
}