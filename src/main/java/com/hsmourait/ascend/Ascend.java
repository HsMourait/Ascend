package com.hsmourait.ascend;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(value = Ascend.MODID, dist = Dist.CLIENT)
public class Ascend {
    public static final String MODID = "ascend";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Ascend(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }
}