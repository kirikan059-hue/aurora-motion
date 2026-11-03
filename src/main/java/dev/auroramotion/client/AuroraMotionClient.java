package dev.auroramotion.client;

import dev.auroramotion.AuroraArmor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

/** Светящиеся частицы вокруг игроков в броне Aurora (чем больше частей, тем ярче). */
public class AuroraMotionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(AuroraMotionClient::tick);
    }

    private static int pieces(Player p) {
        int n = 0;
        if (p.getItemBySlot(EquipmentSlot.HEAD).is(AuroraArmor.HELMET)) n++;
        if (p.getItemBySlot(EquipmentSlot.CHEST).is(AuroraArmor.CHESTPLATE)) n++;
        if (p.getItemBySlot(EquipmentSlot.LEGS).is(AuroraArmor.LEGGINGS)) n++;
        if (p.getItemBySlot(EquipmentSlot.FEET).is(AuroraArmor.BOOTS)) n++;
        return n;
    }

    private static void tick(Minecraft mc) {
        ClientLevel level = mc.level;
        if (level == null || mc.isPaused()) return;

        for (Player p : level.players()) {
            int n = pieces(p);
            if (n == 0) continue;

            RandomSource r = p.getRandom();
            if (r.nextInt(Math.max(1, 10 - n * 2)) != 0) continue;

            double x = p.getX() + (r.nextDouble() - 0.5) * 1.1;
            double y = p.getY() + r.nextDouble() * 1.9;
            double z = p.getZ() + (r.nextDouble() - 0.5) * 1.1;

            level.addParticle(ParticleTypes.END_ROD, x, y, z,
                    0.0, 0.015 + r.nextDouble() * 0.02, 0.0);
            if (n == 4 && r.nextInt(3) == 0) {
                level.addParticle(ParticleTypes.ENCHANT,
                        p.getX(), p.getY() + 1.6, p.getZ(),
                        (r.nextDouble() - 0.5) * 0.6, -0.4, (r.nextDouble() - 0.5) * 0.6);
            }
        }
    }
}
