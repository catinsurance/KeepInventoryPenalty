package fab.keepinventorypenalty.config;

import com.google.gson.Gson;

public class ConfigUpdater
{
    private ConfigData oldConfig;
    public ConfigUpdater(ConfigData config)
    {
        oldConfig = config;
    }

    // This is just a quick and dirty hack
    // Setting the default value does currently not work but will be fine for now...
    public ConfigData UpgradeConfig()
    {
        ConfigData newDefault = ConfigManager.GetDefaultConfig();
        if(oldConfig == null)
        {
            return newDefault;
        }

        Gson gson = new Gson();
        String oldJson = gson.toJson(oldConfig);

        ConfigData newConfig;
        newConfig = gson.fromJson(oldJson, ConfigData.class);
        return newConfig;
    }
}