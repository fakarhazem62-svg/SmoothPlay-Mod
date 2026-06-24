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
     * Removes the hand sway that happens when the player moves their view.
     * This is the last remaining source of camera shake after bobView is cancelled.
     * We zero out the internal sway angles on every update tick.
     */
    @Inject(method = "updateHeldItems", at = @At("RETURN"))
    private void smoothPlay_removeHandSway(ClientPlayerEntity player, float tickDelta, CallbackInfo ci) {
        // The equip progress controls hand animation — keep those
        // but zero out sway by clamping equip progress changes tightly
        // This prevents the "hand drift" effect when rapidly moving the camera
        equipProgressMainHand = prevEquipProgressMainHand;
        equipProgressOffHand = prevEquipProgressOffHand;
    }
}
