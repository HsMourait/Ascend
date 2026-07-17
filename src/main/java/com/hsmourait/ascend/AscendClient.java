package com.hsmourait.ascend;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Ascend.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Ascend.MODID, value = Dist.CLIENT)
public class AscendClient {
    private static final float TOP_PITCH = -49.0f;    // 初始抬头角度
    private static final float BOTTOM_PITCH = 33.0f;   // 停止低头角度

    private enum Phase {
        SNAP_TOP,   // 瞬间调整至 -49°
        LOOK_DOWN,  // 缓慢低头
        HOLD        // 保持 33° 不动
    }

    private static Phase currentPhase = Phase.SNAP_TOP;
    private static float holdTimer = 0;

    public AscendClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        Ascend.LOGGER.info("Ascend client setup complete");
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean isLeftClickDown = mc.options.keyAttack.isDown();

        if (!isLeftClickDown) {
            // 松开左键，重置状态机，让玩家恢复自由视角
            currentPhase = Phase.SNAP_TOP;
            holdTimer = 0;
            return;
        }

        // 状态机运转
        switch (currentPhase) {
            case SNAP_TOP:
                // 瞬间将视角拉至 -49°
                mc.player.setXRot(TOP_PITCH);
                currentPhase = Phase.LOOK_DOWN;
                break;

            case LOOK_DOWN:
                // 每秒匀速低头（20 ticks/秒）
                float speedPerTick = (float) Config.LOOK_DOWN_SPEED.getAsDouble() / 20.0f;
                float newPitch = mc.player.getXRot() + speedPerTick;
                if (newPitch >= BOTTOM_PITCH) {
                    mc.player.setXRot(BOTTOM_PITCH);
                    holdTimer = 0;
                    currentPhase = Phase.HOLD;
                } else {
                    mc.player.setXRot(newPitch);
                }
                break;

            case HOLD:
                // 保持 33°，等待计时结束
                mc.player.setXRot(BOTTOM_PITCH);
                holdTimer += 1.0f / 20.0f;
                float holdDuration = (float) Config.HOLD_DURATION_SECONDS.getAsDouble();
                if (holdTimer >= holdDuration) {
                    currentPhase = Phase.SNAP_TOP;
                }
                break;
        }
    }
}