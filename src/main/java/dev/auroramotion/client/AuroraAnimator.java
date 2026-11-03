package dev.auroramotion.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Вся логика анимации здесь; миксины только вызывают apply(). */
public final class AuroraAnimator {
    private static final Logger LOG = LoggerFactory.getLogger("AuroraMotion");
    private static boolean loggedHook = false;
    private static boolean loggedMove = false;

    private AuroraAnimator() {}

    public static void apply(HumanoidModel<?> m, HumanoidRenderState s) {
        if (!loggedHook) {
            loggedHook = true;
            LOG.info("[AuroraMotion] animation hook ACTIVE on {}", m.getClass().getName());
        }
        if (s.isFallFlying || s.swimAmount > 0.0F) return;

        float speed = Math.min(1.0F, s.walkAnimationSpeed);
        float p = s.walkAnimationPos * 0.6662F;
        float idle = 1.0F - speed;

        if (speed > 0.2F && !loggedMove) {
            loggedMove = true;
            LOG.info("[AuroraMotion] moving: speed={} pos={}", speed, s.walkAnimationPos);
        }

        // дыхание в покое
        float breathe = Mth.sin(s.ageInTicks * 0.067F) * idle;
        m.body.xRot += breathe * 0.03F;
        m.head.xRot += breathe * 0.04F;
        m.rightArm.zRot += breathe * 0.05F;
        m.leftArm.zRot -= breathe * 0.05F;

        // ходьба / бег
        float twist = Mth.cos(p) * 0.22F * speed;
        m.body.yRot += twist;
        m.body.zRot += Mth.sin(p) * 0.10F * speed;
        m.body.xRot += 0.14F * speed;
        m.head.yRot -= twist * 0.8F;
        m.head.zRot -= Mth.sin(p) * 0.08F * speed;
        m.head.xRot -= 0.08F * speed;
        m.rightArm.yRot += twist * 0.6F;
        m.leftArm.yRot += twist * 0.6F;

        if (!s.isUsingItem && s.attackTime <= 0.0F) {
            m.rightArm.xRot += m.rightArm.xRot * 0.5F * speed;
            m.leftArm.xRot += m.leftArm.xRot * 0.5F * speed;
        }
        m.rightLeg.xRot += m.rightLeg.xRot * 0.35F * speed;
        m.leftLeg.xRot += m.leftLeg.xRot * 0.35F * speed;

        // удар
        if (s.attackTime > 0.0F) {
            float a = Mth.sin(s.attackTime * Mth.PI);
            m.body.xRot += a * 0.35F;
            m.body.zRot += a * 0.10F;
            m.head.xRot -= a * 0.20F;
            m.rightLeg.xRot -= a * 0.50F;
            m.leftLeg.xRot += a * 0.35F;
        }

        // оверлеи должны повторять изменённые части
        m.hat.copyFrom(m.head);
        if (m instanceof PlayerModel pm) {
            pm.leftSleeve.copyFrom(pm.leftArm);
            pm.rightSleeve.copyFrom(pm.rightArm);
            pm.leftPants.copyFrom(pm.leftLeg);
            pm.rightPants.copyFrom(pm.rightLeg);
            pm.jacket.copyFrom(pm.body);
        }
    }
}
