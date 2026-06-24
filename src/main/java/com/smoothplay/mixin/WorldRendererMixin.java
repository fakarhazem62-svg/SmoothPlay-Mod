package com.smoothplay.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    /** Skip rain/snow rendering — one of the most expensive render passes */
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true, require = 0)
    private void smoothPlay_skipWeather(CallbackInfo ci) {
        ci.cancel();
    }
}
