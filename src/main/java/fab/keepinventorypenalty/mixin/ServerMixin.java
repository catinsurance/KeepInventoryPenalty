package fab.keepinventorypenalty.mixin;

import fab.keepinventorypenalty.KeepInventoryPenalty;
import fab.keepinventorypenalty.config.ConfigManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class ServerMixin
{
    @Inject(at = @At("TAIL"), method = "loadWorld")
    private void loadingWorld(CallbackInfo ci)
    {
        ConfigManager.LoadConfig((MinecraftServer)(Object)this);
    }

    @Inject(at = @At("TAIL"), method = "reloadResources")
    private void reloadingResources(Collection<String> dataPacks, CallbackInfoReturnable<CompletableFuture<Void>> cir)
    {
        ConfigManager.LoadConfig((MinecraftServer)(Object)this);
    }
}