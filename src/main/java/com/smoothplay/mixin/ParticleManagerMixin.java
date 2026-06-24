package com.smoothplay.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Shadow private Map<ParticleTextureSheet, Queue<Particle>> particles;

    // Absolute maximum particles on screen — Minecraft's default is ~16,000
    private static final int MAX_PARTICLES = 128;

    @Inject(
        method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void smoothPlay_hardCapParticles(CallbackInfoReturnable<Particle> cir) {
        if (particles == null) return;
        int total = 0;
        for (Queue<Particle> queue : particles.values()) {
            total += queue.size();
            if (total >= MAX_PARTICLES) {
                cir.setReturnValue(null);
                return;
            }
        }
    }
}
