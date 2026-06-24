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

    @Shadow
    public GameOptions options;

    private static int tickCounter = 0;

    /**
     * Every 20 ticks (1 second), enforce all performance settings.
     * This prevents other mods or the game from overriding them back.
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void smoothPlay_enforceSettings(CallbackInfo ci) {
        if (options == null) return;

        tickCounter++;
        if (tickCounter % 20 != 0) return;

        // Anti-shake
        options.getBobView().setValue(false);

        // FPS killers — all OFF
        options.getEntityShadows().setValue(false);
        options.getParticles().setValue(ParticlesMode.MINIMAL);
        options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
        options.getBiomeBlendRadius().setValue(0);

        // Fast graphics mode for maximum FPS
        options.getGraphicsMode().setValue(GraphicsMode.FAST);

        // Reduce simulation distance (fewer entities ticking)
        if (options.getSimulationDistance().getValue() > 6) {
            options.getSimulationDistance().setValue(6);
        }
    }
}
