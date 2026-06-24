package com.smoothplay.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /** Cancel walking view bob — the main source of shake while moving */
    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_cancelWalkBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }

    /** Cancel hurt screen tilt — shake when the player takes damage */
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_cancelHurtBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }
}
