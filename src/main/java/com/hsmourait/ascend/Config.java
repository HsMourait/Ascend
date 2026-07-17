package com.hsmourait.ascend;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 低头速度（度/秒）
    public static final ModConfigSpec.DoubleValue LOOK_DOWN_SPEED = BUILDER
            .comment("视角低头速度（度/秒），默认 10.0 度/秒")
            .defineInRange("lookDownSpeed", 10.0, 1.0, 180.0);

    // 在底部角度（33°）保持的时间（秒）
    public static final ModConfigSpec.DoubleValue HOLD_DURATION_SECONDS = BUILDER
            .comment("在 33° 位置保持不动的时长（秒），默认 5.0 秒")
            .defineInRange("holdDurationSeconds", 5.0, 0.0, 60.0);

    static final ModConfigSpec SPEC = BUILDER.build();
}