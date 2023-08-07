package fab.keepinventorypenalty;

import fab.keepinventorypenalty.config.ConfigManager;
import net.minecraft.util.math.random.Random;

public class PenaltyCalculator
{
    public static float DefaultPenalty(int experienceLevel)
    {
        float loss = experienceLevel * ConfigManager.GetConfig().LossPercentage;
        return experienceLevel - loss;
    }

    public static float RandomizedPenalty(int experienceLevel)
    {
        float min = ConfigManager.GetConfig().RandomMin;
        float max = ConfigManager.GetConfig().RandomMax;
        float rand = min + Random.create().nextFloat() * (max - min);

        float loss = experienceLevel * rand;
        return experienceLevel - loss;
    }
}