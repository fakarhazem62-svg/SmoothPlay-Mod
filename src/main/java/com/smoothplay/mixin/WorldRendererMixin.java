package com.smoothplay.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    /**
     * Skip weather rendering (rain/snow particles) entirely.
     * Rain is one of the most expensive rendering operations in Minecraft.
     */
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_skipWeather(CallbackInfo ci) {
        ci.cancel();
    }
}
