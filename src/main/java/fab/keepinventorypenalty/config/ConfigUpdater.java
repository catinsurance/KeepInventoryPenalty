package fab.keepinventorypenalty.config;

import com.google.gson.Gson;

public class ConfigUpdater
{
    private ConfigData oldConfig;
    public ConfigUpdater(ConfigData config)
    {
        oldConfig = config;
    }

    // This is just a quick and dirty hack bc I cant be bothered rn.
    // The new json file will have the saved property values of the old file but
    // setting the default values for the new properties does currently not work but will be fine for now...
    public ConfigData UpgradeConfig()
    {
        //return default config if the loaded config is null just to be safe
        if(oldConfig == null)
        {
            return ConfigManager.GetDefaultConfig();
        }

        Gson gson = new Gson();
        String oldJson = gson.toJson(oldConfig);

        ConfigData newConfig;
        newConfig = gson.fromJson(oldJson, ConfigData.class);
        return newConfig;
    }
}