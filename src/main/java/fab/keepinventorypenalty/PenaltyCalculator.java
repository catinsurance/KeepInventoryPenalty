package fab.keepinventorypenalty;

import fab.keepinventorypenalty.config.ConfigManager;
import net.minecraft.util.math.random.Random;

public class PenaltyCalculator
{
    public static int DefaultPenalty(int experienceLevel)
    {
        return experienceLevel / ConfigManager.GetConfig().LossPercentage;
    }

    public static int RandomizedPenalty(int experienceLevel)
    {
        int rand = Random.create().nextBetween(ConfigManager.GetConfig().RandomMin, ConfigManager.GetConfig().RandomMax);
        return experienceLevel / rand;
    }
}