package org.jade.nodeathanimationsplus.mixin;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import org.jade.nodeathanimationsplus.ModClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private void onShouldRender(Entity entity, Frustum frustum, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
		// Suppressing the render only hides the model - the entity is still there, still ticking, still
		// able to hit you. Same as the existing dead-entity case above; this is a visual filter, not a
		// removal, so nothing about hitboxes or damage changes.
		if (!ModClient.config.renderZombifiedPiglins && entity instanceof ZombifiedPiglin) {
			cir.setReturnValue(false);
			return;
		}

		if (!ModClient.config.deathAnimation && entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() <= 0f) {
			cir.setReturnValue(false);
		}
	}
}
