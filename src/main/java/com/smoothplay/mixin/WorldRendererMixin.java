package com.smoothplay.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    // ── Skip rain/snow rendering entirely ─────────────────────────────────
    // Rain is among the most expensive single-pass renders in Minecraft.
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_skipWeather(CallbackInfo ci) {
        ci.cancel();
    }

    // ── Skip sky rendering when the player is underground ─────────────────
    // Saves a full sky render pass on GPU with zero visual impact.
    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_skipSkyIfUnderground(CallbackInfo ci) {
        net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
        if (client.player != null && client.player.getY() < 0) {
            ci.cancel();
        }
    }
}
