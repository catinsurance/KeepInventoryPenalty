package fab.keepinventorypenalty;

import fab.keepinventorypenalty.config.ConfigManager;
import fab.keepinventorypenalty.config.data.LevelShareMode;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PenaltyManager
{
    public static void SendShameMessage(ServerPlayerEntity instance, int totalLoss)
    {
        String playerName = instance.getDisplayName().getString();
        MutableText text = Text.literal(playerName)
                .append(Text.literal(" died and lost ").formatted()
                        .append(Text.literal(Integer.toString(totalLoss)).formatted(Formatting.RED))
                        .append(Text.literal(" experience levels").formatted()));

        instance.getServer().getPlayerManager().broadcast(text, false);
    }

    public static void SendKillShameMessage(ServerPlayerEntity instance, ServerPlayerEntity attacker, int totalLoss)
    {
        if(attacker == null) return;

        String playerName = instance.getDisplayName().getString();
        String attackerPlayerName = attacker.getDisplayName().getString();
        MutableText text = Text.literal(attackerPlayerName)
                .append(Text.literal(" killed ").formatted()
                        .append(Text.literal(playerName).formatted()
                                .append(Text.literal(" and stole ")).formatted()
                                .append(Text.literal(Integer.toString(totalLoss)).formatted(Formatting.RED))
                                .append(Text.literal(" experience levels").formatted())));

        instance.getServer().getPlayerManager().broadcast(text, false);
    }

    public static void LevelShareFlow(ServerPlayerEntity instance, ServerPlayerEntity attacker, int totalLoss)
    {;
        if(ConfigManager.GetConfig().levelShare.modeOnPlayerKill == LevelShareMode.DISTRIBUTE)
        {
            // TODO: Distribute equally among all current players
        }
        else if(ConfigManager.GetConfig().levelShare.modeOnPlayerKill == LevelShareMode.TAKE)
        {
            attacker.setExperienceLevel(attacker.experienceLevel += totalLoss);
        }
        else if(ConfigManager.GetConfig().levelShare.modeOnPlayerKill == LevelShareMode.LOOSE)
        {
            // Default
        }
    }
}