package fab.keepinventorypenalty.mixin;

import fab.keepinventorypenalty.PenaltyCalculator;
import fab.keepinventorypenalty.config.ConfigManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class DeathMixin
{
	// Unsure whether injecting at the top or bottom (iykyk) of the method is better
	@Inject(at = @At("TAIL"), method = "onDeath")
	private void init(CallbackInfo info)
	{
		if(ConfigManager.GetConfig().PenaltyEnabled)
		{
			// cast triggering player
			ServerPlayerEntity instance = (ServerPlayerEntity) (Object) this;

			if(instance.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY))
			{
				float loss;
				if(ConfigManager.GetConfig().RandomizePenalty)
				{
					loss = PenaltyCalculator.RandomizedPenalty(instance.experienceLevel);
				}
				else
				{
					loss = PenaltyCalculator.DefaultPenalty(instance.experienceLevel);
				}
				int roundedLoss = Math.round(loss);
				int totalLoss = instance.experienceLevel - roundedLoss;

				// Set the player xp level
				instance.setExperienceLevel(Math.round(roundedLoss));

				// Send a global shame message if enabled
				if(ConfigManager.GetConfig().GlobalShame)
				{
					String playerName = instance.getDisplayName().getString();
					MutableText text = Text.literal(playerName)
							.append(Text.literal(" died and lost ").formatted()
							.append(Text.literal(Integer.toString(totalLoss)).formatted(Formatting.RED))
							.append(Text.literal(" experience levels").formatted()));

					instance.getServer().getPlayerManager().broadcast(text, false);
				}
			}
		}
	}
}