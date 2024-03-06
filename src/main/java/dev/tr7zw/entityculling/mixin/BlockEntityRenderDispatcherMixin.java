package dev.tr7zw.entityculling.mixin;

import dev.tr7zw.entityculling.Config;
import dev.tr7zw.entityculling.access.Cullable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.entityculling.EntityCullingMod;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {

    @Inject(method = "method_1278", at = @At("HEAD"), cancellable = true)
    public void render(BlockEntity blockEntity, float par2, CallbackInfo ci) {
        if (Config.Fields.disableBlockEntityCulling) {
            return;
        }
        BlockEntityRenderer blockEntityRenderer = method_1279(blockEntity);
        if (blockEntityRenderer == null) return;
        // respect the "shouldRenderOffScreen" method
        // shouldRenderOffScreen would probably be false in beta
        /*if (blockEntityRenderer != null && blockEntityRenderer.shouldRenderOffScreen(blockEntity)) {
            EntityCullingModBase.instance.renderedBlockEntities++;
            return;
        }*/
        if (!((Cullable) blockEntity).isForcedVisible() && ((Cullable) blockEntity).isCulled()) {
            EntityCullingMod.instance.skippedBlockEntities++;
            ci.cancel();
            return;
        }
        EntityCullingMod.instance.renderedBlockEntities++;
    }

    @Shadow
    public abstract BlockEntityRenderer method_1279(BlockEntity blockEntity);

}
