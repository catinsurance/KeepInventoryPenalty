package fab.keepinventorypenalty.config;

public class ConfigData
{
    public int LossPercentage;
    public boolean PenaltyEnabled;

    public ConfigData(int lossPercentage, boolean penaltyEnabled)
    {
        LossPercentage = lossPercentage;
        PenaltyEnabled = penaltyEnabled;
    }
}