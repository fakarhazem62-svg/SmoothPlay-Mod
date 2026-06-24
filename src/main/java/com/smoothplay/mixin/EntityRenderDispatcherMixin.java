package com.smoothplay.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    private static final double MAX_DIST_SQ = 48.0 * 48.0;

    /**
     * Skip entities beyond 48 blocks — reduces entity render workload significantly.
     * Uses shouldRender which is reliably present across 1.20.x–1.21.x.
     */
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, require = 0)
    private <E extends Entity> void smoothPlay_cullDistant(
        E entity, net.minecraft.client.render.Frustum frustum,
        double x, double y, double z, CallbackInfo ci
    ) {
        double dx = x - entity.getX();
        double dy = y - entity.getY();
        double dz = z - entity.getZ();
        if (!entity.isPlayer() && dx*dx + dy*dy + dz*dz > MAX_DIST_SQ) {
            ci.cancel();
        }
    }
}
