package fab.keepinventorypenalty;

import fab.keepinventorypenalty.config.ConfigManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

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

    public static void GiveAttackerXP(ServerPlayerEntity instance, DamageSource source, int totalLoss)
    {
        if(source.getAttacker() == null) return;

        if(source.getAttacker().isPlayer())
        {
            ServerPlayerEntity attacker = (ServerPlayerEntity) source.getAttacker();

            attacker.setExperienceLevel(attacker.experienceLevel += totalLoss);

            // shame message if enabled
            if(ConfigManager.GetConfig().globalShame)
                SendAttackShameMessage(instance, attacker, totalLoss);
        }
    }

    public static void DistributeAmongPlayers(ServerPlayerEntity instance, int totalLoss)
    {
        List<ServerPlayerEntity> players = instance.server.getPlayerManager().getPlayerList();
        int playerCount = instance.server.getPlayerManager().getCurrentPlayerCount() - 1;
        int splitLoss = totalLoss / playerCount;

        for (ServerPlayerEntity current : players)
        {
            if(current == instance) continue;

            current.setExperienceLevel(current.experienceLevel += splitLoss);
        }

        // shame message if enabled
        if(ConfigManager.GetConfig().globalShame)
            SendDistributionShameMessage(instance, splitLoss);
    }
}