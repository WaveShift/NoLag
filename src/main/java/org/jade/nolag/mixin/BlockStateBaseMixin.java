package org.jade.nolag.mixin;

import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jade.nolag.ModClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Reports the nether portal block as INVISIBLE so nothing draws it.
 * <p>
 * This replaces an earlier hook on {@code BlockRenderDispatcher.renderBatched}, which compiled and
 * bound correctly but had no visible effect. Two reasons, and this fixes both:
 * <p>
 * 1. renderBatched is only reached through vanilla's SectionCompiler. Any replacement chunk mesher -
 * Sodium and its forks being the obvious ones on a client tuned for performance - builds geometry
 * itself and never calls it, so the injection sat there as dead code. getRenderShape is different:
 * every mesher has to consult it, because it is how vanilla marks air and barriers as not-drawn.
 * <p>
 * 2. Cancelling the draw call only affects meshes built after the change, so chunks already compiled
 * kept the portal until something forced a rebuild. ModClient now calls levelRenderer.allChanged()
 * when the config is saved, so toggling this applies immediately rather than whenever you next
 * walked far enough away and back.
 * <p>
 * INVISIBLE only removes the geometry. The portal still exists and still teleports you.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
	@Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
	private void onGetRenderShape(CallbackInfoReturnable<RenderShape> cir) {
		if (!ModClient.config.renderNetherPortalBlocks && ((BlockState) (Object) this).getBlock() instanceof NetherPortalBlock) {
			cir.setReturnValue(RenderShape.INVISIBLE);
		}
	}
}
