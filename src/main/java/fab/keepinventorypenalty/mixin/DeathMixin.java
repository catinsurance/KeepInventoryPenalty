package fab.keepinventorypenalty.mixin;

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
				int loss = instance.experienceLevel / ConfigManager.GetConfig().LossPercentage;
				int totalLoss = instance.experienceLevel - loss;
				instance.setExperienceLevel(loss);

				MutableText text = Text.translatable("chat.keepinventorypenalty.death_1").formatted()
						.append(Text.literal(Integer.toString(totalLoss)).formatted(Formatting.RED))
						.append(Text.translatable("chat.keepinventorypenalty.death_2").formatted());
				instance.sendMessage(text);
			}
		}
	}
}