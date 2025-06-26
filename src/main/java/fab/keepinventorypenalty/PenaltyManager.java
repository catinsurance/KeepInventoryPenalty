package fab.keepinventorypenalty;

import fab.keepinventorypenalty.config.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class PenaltyManager
{
    public static void SendLocalDeathMessage(ServerPlayerEntity instance, int totalLoss)
    {
        MutableText text = Text.literal("You died and lost ")
                        .append(Text.literal(Integer.toString(totalLoss)).formatted(Formatting.RED))
                        .append(Text.literal(" experience levels").formatted());

        instance.sendMessage(text);
    }

    public static void SendShameMessage(ServerPlayerEntity instance, int totalLoss)
    {
        String playerName = instance.getDisplayName().getString();
        MutableText text = Text.literal(playerName)
                .append(Text.literal(" died and lost ").formatted()
                        .append(Text.literal(Integer.toString(totalLoss)).formatted(Formatting.RED))
                        .append(Text.literal(" experience levels").formatted()));

        instance.getServer().getPlayerManager().broadcast(text, false);
    }

    public static void SendAttackShameMessage(ServerPlayerEntity instance, ServerPlayerEntity attacker, int totalLoss)
    {
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

    public static void SendDistributionShameMessage(ServerPlayerEntity instance, int splitLoss)
    {
        String playerName = instance.getDisplayName().getString();
        MutableText text = Text.literal(playerName)
                .append(Text.literal(" died and distributed ").formatted()
                        .append(Text.literal(Integer.toString(splitLoss)).formatted(Formatting.RED))
                        .append(Text.literal(" experience levels among all players!").formatted()));

        instance.getServer().getPlayerManager().broadcast(text, false);
    }

    public static void GiveAttackerXP(ServerPlayerEntity instance, Entity attacker, int totalLoss)
    {
        if(!attacker.isPlayer()) return;

        ServerPlayerEntity playerAttacker = (ServerPlayerEntity) attacker;

        playerAttacker.setExperienceLevel(playerAttacker.experienceLevel += totalLoss);

        // shame message if enabled
        if(ConfigManager.GetConfig().globalShame)
            SendAttackShameMessage(instance, playerAttacker, totalLoss);
    }

    public static void DistributeAmongPlayers(ServerPlayerEntity instance, int totalLoss)
    {
        List<ServerPlayerEntity> players = instance.getServer().getPlayerManager().getPlayerList();
        int playerCount = instance.getServer().getPlayerManager().getCurrentPlayerCount() - 1;

        float splitLoss = (float) totalLoss / playerCount;
        int roundedSplit = Math.round(splitLoss);
        if(roundedSplit <= 0)
        {
            roundedSplit = 1;
        }

        for (ServerPlayerEntity current : players)
        {
            if(current == instance) continue;

            current.setExperienceLevel(current.experienceLevel += roundedSplit);
        }

        // shame message if enabled
        if(ConfigManager.GetConfig().globalShame)
            SendDistributionShameMessage(instance, roundedSplit);
    }
}