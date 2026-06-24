package com.smoothplay.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /**
     * Cancels the view bob when the player walks.
     * Prevents screen from swaying left/right while moving.
     */
    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_cancelWalkBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }

    /**
     * Cancels the screen tilt when the player takes damage.
     * Keeps the camera perfectly still even when hit.
     */
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_cancelHurtBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }
}
