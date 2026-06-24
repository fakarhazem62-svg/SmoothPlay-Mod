package com.smoothplay.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    private static final double MAX_ENTITY_RENDER_DIST_SQ = 64.0 * 64.0; // 64 blocks

    /**
     * Skip rendering entities that are very far away.
     * Pairs with EntityCulling for maximum entity render savings.
     */
    @Inject(
        method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private <E extends Entity> void smoothPlay_cullDistantEntities(
        E entity, double x, double y, double z, float tickDelta,
        Object matrices, Object vertexConsumers, int light,
        CallbackInfo ci
    ) {
        Vec3d pos = entity.getPos();
        double dx = x - pos.x;
        double dy = y - pos.y;
        double dz = z - pos.z;
        // Skip non-player entities beyond 64 blocks
        if (!entity.isPlayer() && (dx * dx + dy * dy + dz * dz) > MAX_ENTITY_RENDER_DIST_SQ) {
            ci.cancel();
        }
    }
}
