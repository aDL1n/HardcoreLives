package pl.cramber.hardcorelives.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LivesLoader {
    private final File file;
    private FileConfiguration config;

    public LivesLoader(File dataFolder) {
        this.file = new File(dataFolder, "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                //add exception logging
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public int load(UUID uuid, int defaultValue) {
        return config.getInt(uuid.toString(), defaultValue);
    }

    public void save(UUID uuid, int lives) {
        config.set(uuid.toString(), lives);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
