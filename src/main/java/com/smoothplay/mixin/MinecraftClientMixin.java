package com.smoothplay.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.ParticlesMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow public GameOptions options;

    private static int tickCounter = 0;
    private static final int TARGET_FPS   = 60;
    private static final int MIN_CHUNKS   = 4;
    private static final int MAX_CHUNKS   = 12;

    @Inject(method = "tick", at = @At("HEAD"))
    private void smoothPlay_enforceSettings(CallbackInfo ci) {
        if (options == null) return;
        tickCounter++;

        // ── Every tick: critical anti-shake ────────────────────────────────
        options.getBobView().setValue(false);

        // ── Every second: full settings pass ───────────────────────────────
        if (tickCounter % 20 == 0) {
            options.getEntityShadows().setValue(false);
            options.getParticles().setValue(ParticlesMode.MINIMAL);
            options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
            options.getBiomeBlendRadius().setValue(0);
            options.getGraphicsMode().setValue(GraphicsMode.FAST);
        }

        // ── Every 2 seconds: dynamic render distance ────────────────────────
        // If FPS is low → shrink view distance; if FPS is high → expand it
        if (tickCounter % 40 == 0) {
            MinecraftClient client = MinecraftClient.getInstance();
            int fps = client.getCurrentFps();
            int currentRD = options.getViewDistance().getValue();

            if (fps < TARGET_FPS && currentRD > MIN_CHUNKS) {
                // FPS too low — reduce view distance by 1
                options.getViewDistance().setValue(currentRD - 1);
            } else if (fps > TARGET_FPS * 2 && currentRD < MAX_CHUNKS) {
                // FPS is great — we can afford to extend view distance by 1
                options.getViewDistance().setValue(currentRD + 1);
            }
        }
    }
}
