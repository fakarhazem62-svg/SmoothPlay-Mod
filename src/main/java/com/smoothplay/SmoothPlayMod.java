package com.smoothplay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class SmoothPlayMod implements ClientModInitializer {

    public static final String MOD_ID = "smoothplay";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("[SmoothPlay] Loaded — Anti-Shake + FPS Boost + Vivid Colors active.");
    }
}
