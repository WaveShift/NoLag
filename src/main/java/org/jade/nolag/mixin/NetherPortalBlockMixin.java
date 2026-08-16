package org.jade.nolag.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jade.nolag.ModClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stops the portal's ambient purple particles and its idle noise.
 * <p>
 * animateTick is the client's random display tick - it only ever runs client-side and does nothing but
 * spawn particles and play the ambient sound, so cancelling it has no gameplay effect at all.
 * <p>
 * Note this covers the particles the BLOCK emits. Particles drawn while the player is stood inside a
 * portal come from the portal screen overlay, which is a separate path and is left alone.
 */
@Mixin(NetherPortalBlock.class)
@Environment(EnvType.CLIENT)
public class NetherPortalBlockMixin {
	@Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
	private void onAnimateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
		if (!ModClient.config.netherPortalParticles) {
			ci.cancel();
		}
	}
}
