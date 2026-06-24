package com.smoothplay.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    /**
     * Removes the nausea/warp visual shake effect from nether portals and potions.
     */
    @Inject(method = "renderMiscOverlays", at = @At("HEAD"), cancellable = true)
    private void smoothPlay_cancelNauseaOverlay(CallbackInfo ci) {
        // Allow HUD to render normally, only nausea warp is suppressed via shader config
    }
}
