package org.jade.nodeathanimationsplus.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jade.nodeathanimationsplus.ModClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Drops the nether portal block from the chunk mesh entirely.
 * <p>
 * Hooked here rather than at {@code getRenderShape} deliberately: BlockRenderDispatcher lives in
 * {@code net.minecraft.client.renderer}, so it is client-only by construction and cancelling it cannot
 * reach collision, teleport logic, or anything the server evaluates. getRenderShape would have worked
 * too, but it sits on BlockBehaviour - shared code - and this mod has no reason to be there.
 * <p>
 * This runs during chunk meshing, not per frame, so the instanceof costs a rebuild rather than a tick.
 * The portal still exists and still teleports you; it is only not drawn.
 */
@Mixin(BlockRenderDispatcher.class)
public class BlockRenderDispatcherMixin {
	@Inject(method = "renderBatched", at = @At("HEAD"), cancellable = true)
	private void onRenderBatched(BlockState blockState, BlockPos blockPos, BlockAndTintGetter blockAndTintGetter,
	                             PoseStack poseStack, VertexConsumer vertexConsumer, boolean bl,
	                             RandomSource randomSource, CallbackInfo ci) {
		if (!ModClient.config.renderNetherPortalBlocks && blockState.getBlock() instanceof NetherPortalBlock) {
			ci.cancel();
		}
	}
}
