package com.smoothplay.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow private double cursorDeltaX;
    @Shadow private double cursorDeltaY;

    // Smoothed values that carry over between frames
    private double smoothX = 0.0;
    private double smoothY = 0.0;

    /**
     * Adaptive input smoothing — applied before the game reads mouse/touch deltas.
     *
     * - Tiny movements (hand shake, < 1.5 px):  90% filtered  → nearly invisible
     * - Normal movements (2–10 px):              50% smoothed  → feels fluid
     * - Fast swipes (> 10 px):                   10% smoothed  → instant response
     *
     * Result: unsteady hands produce a stable, cinematic camera while
     * intentional fast turns still respond instantly.
     */
    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void smoothPlay_adaptiveInputSmoothing(CallbackInfo ci) {
        double magnitude = Math.sqrt(cursorDeltaX * cursorDeltaX + cursorDeltaY * cursorDeltaY);

        // Choose smoothing factor based on movement size
        double alpha;
        if (magnitude < 1.5) {
            // Micro-jitter / hand shake — kill almost completely
            alpha = 0.10;
        } else if (magnitude < 10.0) {
            // Normal aiming / looking around — smooth but responsive
            alpha = 0.55;
        } else {
            // Fast swipe — near-instant (only light smoothing)
            alpha = 0.88;
        }

        // Exponential moving average: new = old * (1-alpha) + raw * alpha
        smoothX = smoothX * (1.0 - alpha) + cursorDeltaX * alpha;
        smoothY = smoothY * (1.0 - alpha) + cursorDeltaY * alpha;

        // Dead-zone: if the smoothed value is tiny, snap to zero
        // This eliminates residual drift after the player stops moving
        if (Math.abs(smoothX) < 0.25) smoothX = 0.0;
        if (Math.abs(smoothY) < 0.25) smoothY = 0.0;

        // Write smoothed deltas back — the game will use these instead of raw input
        cursorDeltaX = smoothX;
        cursorDeltaY = smoothY;
    }
}
