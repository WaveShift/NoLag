package org.jade.nolag.mixin;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.jade.nolag.ModClient.config;

/**
 * Ported from 1.20.4. Two things changed in 1.21.2's entity-render-state refactor:
 * <p>
 * 1. {@code getFlipDegrees} no longer takes the entity. It was {@code getFlipDegrees(T livingEntity)}
 * and is now a no-arg {@code protected float getFlipDegrees()} - the renderer reads what it needs off
 * the render state instead. The injected handler has to match that, or mixin fails to apply it.
 * <p>
 * 2. {@code LivingEntityRenderer} gained type parameters: it is now {@code <T, S, M>} extending
 * {@code EntityRenderer<T, S>}, where S is the render state and M the model. The old mixin declared
 * {@code extends EntityRenderer<T>} to inherit the constructor, which no longer compiles.
 * <p>
 * Rather than restate all three parameters, the class declaration is dropped entirely - this mixin
 * only injects and never touches an inherited member, so it needs neither the generics nor the
 * superclass. That also means it will not break again on the next signature shuffle.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "getFlipDegrees", at = @At("HEAD"), cancellable = true)
	void customFlipDegrees(CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue((float) config.deathFlipDegrees);
	}
}
