package fab.keepinventorypenalty.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class DeathMixin
{
	// Unsure whether injecting at the top or bottom of the method is better
	@Inject(at = @At("TAIL"), method = "onDeath")
	private void init(CallbackInfo info)
	{

	}
}