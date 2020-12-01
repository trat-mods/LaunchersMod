package net.launchers.mod.loader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.launchers.mod.entity_renderer.ExtremeLauncherBlockEntityRenderer;
import net.launchers.mod.entity_renderer.LauncherBlockEntityRenderer;
import net.launchers.mod.entity_renderer.PoweredLauncherBlockEntityRenderer;
import net.launchers.mod.network.packet.CustomPacket;
import net.launchers.mod.network.packet.UnboundedEntityVelocityS2CPacket;

import static net.launchers.mod.initializer.LMEntities.*;

public final class ClientLoader implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        BlockEntityRendererRegistry.INSTANCE.register(LAUNCHER_BLOCK_ENTITY, LauncherBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(PW_LAUNCHER_BLOCK_ENTITY, PoweredLauncherBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(EX_LAUNCHER_BLOCK_ENTITY, ExtremeLauncherBlockEntityRenderer::new);
        ClientSidePacketRegistry.INSTANCE.register(CustomPacket.PACKET_ID, (ctx, buf) -> UnboundedEntityVelocityS2CPacket.read(buf).onReceive(ctx));
    }
}
