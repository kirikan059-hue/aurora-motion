package dev.auroramotion.mixin.client;

import dev.auroramotion.client.AuroraAnimator;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Игрок и (в 26.2) слои брони, которые тоже PlayerModel. Хук в самом конце setupAnim. */
@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin {
    @Inject(
        method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V",
        at = @At("TAIL"))
    private void aurora$animate(AvatarRenderState state, CallbackInfo ci) {
        AuroraAnimator.apply((HumanoidModel<?>) (Object) this, state);
    }
}
