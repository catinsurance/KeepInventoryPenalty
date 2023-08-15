package fab.keepinventorypenalty.mixin;

import fab.keepinventorypenalty.PenaltyCalculator;
import fab.keepinventorypenalty.PenaltyManager;
import fab.keepinventorypenalty.config.ConfigManager;
import fab.keepinventorypenalty.config.data.LevelShare;
import fab.keepinventorypenalty.config.data.LevelShareMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class DeathMixin
{
	@Shadow public abstract boolean damage(DamageSource source, float amount);

	@Shadow public abstract void attack(Entity target);

	@Shadow public abstract void increaseStat(Stat<?> stat, int amount);

	// Unsure whether injecting at the top or bottom (iykyk) of the method is better
	@Inject(at = @At("TAIL"), method = "onDeath")
	private void Death(DamageSource damageSource, CallbackInfo info)
	{
		// check if penalty is enabled
		if(ConfigManager.GetConfig().penalty.enabled)
		{
			// get player
			ServerPlayerEntity instance = (ServerPlayerEntity) (Object) this;

			Entity attacker = damageSource.getAttacker();

			LevelShare levelShare = ConfigManager.GetConfig().levelShare;

			// check if keep inventory is enabled
			if(instance.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY))
			{
				float loss;
				// random or set penalty amount depending on config
				if(ConfigManager.GetConfig().penalty.randomizer.enabled)
				{
					loss = PenaltyCalculator.RandomizedPenalty(instance.experienceLevel);
				}
				else
				{
					loss = PenaltyCalculator.DefaultPenalty(instance.experienceLevel);
				}

				int roundedLoss = Math.round(loss);
				int totalLoss = instance.experienceLevel - roundedLoss;

				// check for type of level share if the attacker is a player
				if(attacker != null	&& attacker.isPlayer())
				{
					if(levelShare.modeOnPlayerKill == LevelShareMode.TAKE)
					{
						// give the attacker the xp
						PenaltyManager.GiveAttackerXP(instance, attacker, totalLoss);
					}
					else if (levelShare.modeOnPlayerKill == LevelShareMode.DISTRIBUTE)
					{
						// give all players a split amount of the xp
						PenaltyManager.DistributeAmongPlayers(instance, totalLoss);
					}
					else if (levelShare.modeOnPlayerKill == LevelShareMode.LOOSE)
					{
						// regular xp penalty. just send a shame message if enabled
						if(ConfigManager.GetConfig().globalShame)
							PenaltyManager.SendShameMessage(instance, totalLoss);
					}
				}
				else // the attacker is no player. regular xp penalty
				{
					// Default. just send a shame message if enabled
					if(ConfigManager.GetConfig().globalShame)
						PenaltyManager.SendShameMessage(instance, totalLoss);
				}

				// set player xp level
				instance.setExperienceLevel(roundedLoss);

				// if global shaming is disabled, send a local message informing the player
				if(!ConfigManager.GetConfig().globalShame)
					PenaltyManager.SendLocalDeathMessage(instance, totalLoss);
			}
		}
	}
}