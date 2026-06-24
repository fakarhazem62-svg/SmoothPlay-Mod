package com.smoothplay.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    public GameOptions options;

    /**
     * On every tick, enforce:
     * - bobView = false  (no head bobbing / shake)
     * - entityShadows = false (minor FPS gain)
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void smoothPlay_enforceSettings(CallbackInfo ci) {
        if (options != null) {
            options.getBobView().setValue(false);
            options.getEntityShadows().setValue(false);
        }
    }
}
