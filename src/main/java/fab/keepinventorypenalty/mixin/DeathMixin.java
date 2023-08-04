package fab.keepinventorypenalty.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class DeathMixin
{
	// Move this to the config file later
	private final int lossPercentage = 2;

	// Unsure whether injecting at the top or bottom (iykyk) of the method is better
	@Inject(at = @At("TAIL"), method = "onDeath")
	private void init(CallbackInfo info)
	{
		// cast triggering player
		ServerPlayerEntity instance = (ServerPlayerEntity) (Object) this;

		if(instance.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY))
		{
			int totalLoss = instance.experienceLevel / lossPercentage;
			instance.setExperienceLevel(instance.experienceLevel - totalLoss);
		}
	}
}