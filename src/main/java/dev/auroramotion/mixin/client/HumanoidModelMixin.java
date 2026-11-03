package dev.auroramotion.mixin.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Добавляет поверх ванильной анимации: поворот корпуса при ходьбе, наклон вперёд на бегу,
 * покачивание, дыхание, «замах» корпусом при ударе. Работает на игроках, зомби, скелетах и
 * на слоях брони (они тоже HumanoidModel), поэтому броня двигается вместе с телом.
 */
@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart body;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightLeg;
    @Shadow @Final public ModelPart leftLeg;

    @Inject(
        method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
        at = @At("TAIL"))
    private void aurora$animate(HumanoidRenderState state, CallbackInfo ci) {
        if (state.isFallFlying || state.swimAmount > 0.0F) return;

        float speed = Math.min(1.0F, state.walkAnimationSpeed);
        float p = state.walkAnimationPos * 0.6662F;
        float idle = 1.0F - speed;

        // дыхание в покое
        float breathe = Mth.sin(state.ageInTicks * 0.067F) * idle;
        body.xRot += breathe * 0.015F;
        head.xRot += breathe * 0.02F;

        // ходьба / бег
        float twist = Mth.cos(p) * 0.10F * speed;
        body.yRot += twist;
        body.zRot += Mth.sin(p) * 0.05F * speed;
        body.xRot += 0.09F * speed;                 // наклон корпуса вперёд
        head.yRot -= twist * 0.8F;                  // голова стабилизируется
        head.zRot -= Mth.sin(p) * 0.04F * speed;
        head.xRot -= 0.05F * speed;
        rightArm.yRot += twist * 0.6F;
        leftArm.yRot += twist * 0.6F;

        if (!state.isUsingItem && state.attackTime <= 0.0F) {
            rightArm.xRot += rightArm.xRot * 0.30F * speed;   // шире взмах рук
            leftArm.xRot += leftArm.xRot * 0.30F * speed;
        }
        rightLeg.xRot += rightLeg.xRot * 0.25F * speed;       // шире шаг
        leftLeg.xRot += leftLeg.xRot * 0.25F * speed;

        // удар: корпус вкладывается в замах, выпад ногами
        if (state.attackTime > 0.0F) {
            float s = Mth.sin(state.attackTime * Mth.PI);
            body.xRot += s * 0.18F;
            body.zRot += s * 0.05F;
            head.xRot -= s * 0.10F;
            rightLeg.xRot -= s * 0.30F;
            leftLeg.xRot += s * 0.20F;
        }
    }
}
