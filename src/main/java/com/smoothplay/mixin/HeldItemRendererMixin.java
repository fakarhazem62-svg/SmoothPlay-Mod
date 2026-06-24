package com.smoothplay.mixin;

import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Shadow private float prevEquipProgressMainHand;
    @Shadow private float equipProgressMainHand;
    @Shadow private float prevEquipProgressOffHand;
    @Shadow private float equipProgressOffHand;

    /**
     * updateHeldItems() takes NO parameters in 1.21.1 — inject only gets CallbackInfo.
     * Freezes equip progress so the hand stays still (no sway on camera move).
     */
    @Inject(method = "updateHeldItems", at = @At("RETURN"), require = 0)
    private void smoothPlay_freezeHandSway(CallbackInfo ci) {
        equipProgressMainHand = prevEquipProgressMainHand;
        equipProgressOffHand  = prevEquipProgressOffHand;
    }
}
