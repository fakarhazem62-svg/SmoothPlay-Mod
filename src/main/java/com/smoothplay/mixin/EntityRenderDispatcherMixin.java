package com.smoothplay.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.Frustum;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    private static final double MAX_DIST_SQ = 48.0 * 48.0;

    /**
     * shouldRender returns boolean — must use CallbackInfoReturnable<Boolean>.
     * Skips non-player entities beyond 48 blocks to reduce render workload.
     */
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, require = 0)
    private <E extends Entity> void smoothPlay_cullDistant(
        E entity, Frustum frustum,
        double x, double y, double z,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (!entity.isPlayer()) {
            double dx = x - entity.getX();
            double dy = y - entity.getY();
            double dz = z - entity.getZ();
            if (dx * dx + dy * dy + dz * dz > MAX_DIST_SQ) {
                cir.setReturnValue(false);
            }
        }
    }
}
