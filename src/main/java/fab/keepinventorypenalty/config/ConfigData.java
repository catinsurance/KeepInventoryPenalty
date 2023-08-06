package fab.keepinventorypenalty.config;

public class ConfigData
{
    public int VERSION_DONT_CHANGE;
    public int LossPercentage = 2;
    public boolean PenaltyEnabled = true;
    public boolean RandomizePenalty = false;
    public int RandomMin = 2;
    public int RandomMax = 4;
    public boolean GlobalShame = false;

    public ConfigData(int version)
    {
        VERSION_DONT_CHANGE = version;
    }

    public ConfigData(int version, int lossPercentage, boolean penaltyEnabled, boolean randomizePenalty, int randomMin,
                      int randomMax, boolean globalShame)
    {
        VERSION_DONT_CHANGE = version;
        LossPercentage = lossPercentage;
        PenaltyEnabled = penaltyEnabled;
        RandomizePenalty = randomizePenalty;
        RandomMin = randomMin;
        RandomMax = randomMax;
        GlobalShame = globalShame;
    }
}