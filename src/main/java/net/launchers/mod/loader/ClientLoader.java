package net.launchers.mod.loader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.launchers.mod.entity_renderer.ExtremeLauncherBlockEntityRenderer;
import net.launchers.mod.entity_renderer.LauncherBlockEntityRenderer;
import net.launchers.mod.entity_renderer.PoweredLauncherBlockEntityRenderer;
import net.launchers.mod.network.packet.UnboundedEntityVelocityPayload;

import static net.launchers.mod.initializer.LMEntities.*;

public final class ClientLoader implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(LAUNCHER_BLOCK_ENTITY, LauncherBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(PW_LAUNCHER_BLOCK_ENTITY, PoweredLauncherBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(EX_LAUNCHER_BLOCK_ENTITY, ExtremeLauncherBlockEntityRenderer::new);
        PayloadTypeRegistry.playS2C().register(UnboundedEntityVelocityPayload.ID, UnboundedEntityVelocityPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(UnboundedEntityVelocityPayload.ID, ((payload, context) -> context.client().execute(() -> {
            payload.onReceive(context.client());
        })));
    }
}
