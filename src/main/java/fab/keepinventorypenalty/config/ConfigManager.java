package fab.keepinventorypenalty.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fab.keepinventorypenalty.KeepInventoryPenalty;
import fab.keepinventorypenalty.config.data.ConfigData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager
{
    // INCREMENT BY 1 WHEN THE CONFIG FILE CHANGED
    public static int CONFIG_VERSION = 2;
    public static String file = "keepInventoryPenalty.json";
    private static Path getSaveFolder(MinecraftServer server)
    {
        return server.getSavePath(WorldSavePath.ROOT).getParent();
    }
    private static Path getSaveFile(MinecraftServer server)
    {
        return getSaveFolder(server).resolve(file);
    }

    private static ConfigData CONFIG = new ConfigData();

    public static ConfigData GetConfig()
    {
        if(CONFIG == null)
        {
            CONFIG = new ConfigData();
        }
        return CONFIG;
    }

    public static void LoadConfig(MinecraftServer server)
    {
        Path path = getSaveFile(server);

        if(Files.exists(path))
        {
            String file = getFile(server);

            Gson gson = new Gson();
            ConfigData data = gson.fromJson(file, ConfigData.class);
            if(data == null)
            {
                CONFIG = new ConfigData();
                return;
            }

            if(data.VERSION_DONT_CHANGE < CONFIG_VERSION)
            {
                KeepInventoryPenalty.LOGGER.info("DETECTED OLDER CONFIG");
                ConfigUpdater updater = new ConfigUpdater(data);

                CONFIG = updater.UpgradeConfig();

                // Save new config
                SaveConfig(server, data);
            }
            else
            {
                CONFIG = data;
            }

            KeepInventoryPenalty.LOGGER.info("SUCCESSFULLY LOADED CONFIG");
        }
        else
        {
            SaveConfig(server, new ConfigData());
        }
    }

    private static void SaveConfig(MinecraftServer server, ConfigData data)
    {
        try
        {
            if(data == null)
                data = new ConfigData();

            data.VERSION_DONT_CHANGE = CONFIG_VERSION;

            BufferedWriter writer = new BufferedWriter(new FileWriter(getSaveFile(server).toString()));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String saveString = gson.toJson(data);

            writer.write(saveString);
            writer.close();
            CONFIG = data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String getFile(MinecraftServer server)
    {
        try
        {
            Path path = getSaveFile(server);

            if (!Files.exists(path))
                return null;
            return Files.readString(path);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}