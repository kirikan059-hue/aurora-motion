package dev.auroramotion.mixin.client;

import dev.auroramotion.client.AuroraAnimator;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Мобы и слои брони (если они HumanoidModel). Игроков обрабатывает PlayerModelMixin. */
@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Inject(
        method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
        at = @At("TAIL"))
    private void aurora$animate(HumanoidRenderState state, CallbackInfo ci) {
        if ((Object) this instanceof PlayerModel) return; // избегаем двойного применения
        AuroraAnimator.apply((HumanoidModel<?>) (Object) this, state);
    }
}
