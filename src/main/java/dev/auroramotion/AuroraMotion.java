package dev.auroramotion;

import net.fabricmc.api.ModInitializer;

public class AuroraMotion implements ModInitializer {
    public static final String MOD_ID = "aurora_motion";

    @Override
    public void onInitialize() {
        AuroraArmor.init();
    }
}
