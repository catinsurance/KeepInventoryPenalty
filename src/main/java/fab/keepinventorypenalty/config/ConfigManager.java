package fab.keepinventorypenalty.config;

import com.google.gson.Gson;
import fab.keepinventorypenalty.KeepInventoryPenalty;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager
{
    public static String file = "keepInventoryPenalty.json";
    private static Path getSaveFolder(MinecraftServer server)
    {
        return server.getSavePath(WorldSavePath.ROOT).getParent();
    }
    private static Path getSaveFile(MinecraftServer server)
    {
        return getSaveFolder(server).resolve(file);
    }

    private static ConfigData CONFIG;

    public static ConfigData GetConfig()
    {
        if(CONFIG == null)
        {
            CONFIG = GetDefaultConfig();
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
                CONFIG = GetDefaultConfig();
                KeepInventoryPenalty.LOGGER.info("JSON PARSED CONFIG WAS NULL? LOADING DEFAULT CONFIG");
            }
            else
                CONFIG = data;

            KeepInventoryPenalty.LOGGER.info("SUCCESSFULLY LOADED CONFIG");
        }
        else
        {
            SaveConfig(server);
        }
    }

    private static void SaveConfig(MinecraftServer server)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getSaveFile(server).toString()));

            ConfigData data = GetDefaultConfig();
            Gson gson = new Gson();
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

    private static ConfigData GetDefaultConfig()
    {
        return new ConfigData(2, true);
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