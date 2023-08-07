package fab.keepinventorypenalty.config;

public class ConfigData
{
    public int VERSION_DONT_CHANGE;
    public float LossPercentage;
    public boolean PenaltyEnabled;
    public boolean RandomizePenalty;
    public float RandomMin;
    public float RandomMax;
    public boolean GlobalShame;

    public ConfigData(int version, float lossPercentage, boolean penaltyEnabled, boolean randomizePenalty, float randomMin,
                      float randomMax, boolean globalShame)
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