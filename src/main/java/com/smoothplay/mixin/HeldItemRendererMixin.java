package com.smoothplay.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
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
     * Freeze equip progress values so the hand doesn't drift when moving camera.
     * Removes the last source of hand sway that bobView doesn't cover.
     */
    @Inject(method = "updateHeldItems", at = @At("RETURN"), require = 0)
    private void smoothPlay_freezeHandSway(ClientPlayerEntity player, float tickDelta, CallbackInfo ci) {
        equipProgressMainHand = prevEquipProgressMainHand;
        equipProgressOffHand  = prevEquipProgressOffHand;
    }
}
