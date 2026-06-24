package com.smoothplay.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    // ── 1. Walking shake ────────────────────────────────────────────────────
    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_cancelWalkBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }

    // ── 2. Damage/hurt screen tilt ──────────────────────────────────────────
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_cancelHurtBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }

    // ── 3. Nausea / portal distortion / potion warping ─────────────────────
    @Inject(method = "getNauseaStrength", at = @At("RETURN"), cancellable = true)
    private void smoothPlay_zeroNausea(float tickDelta, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.0f);
    }

    // ── 4. Sprint FOV zoom-in (the "lunge" feeling) ─────────────────────────
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void smoothPlay_lockFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        // Clamp FOV to base value — prevents sprint zoom-in shake feeling
        double baseFov = net.minecraft.client.MinecraftClient.getInstance().options.getFov().getValue();
        double current = cir.getReturnValueD();
        // Allow at most ±1 degree deviation from base FOV
        double clamped = Math.max(baseFov - 1, Math.min(baseFov + 1, current));
        cir.setReturnValue(clamped);
    }
}
