package fab.keepinventorypenalty.config;

public class ConfigData
{
    public int VERSION_DONT_CHANGE;
    public int LossPercentage;
    public boolean PenaltyEnabled;
    public boolean RandomizePenalty;
    public int RandomMin;
    public int RandomMax;
    public boolean GlobalShame;

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