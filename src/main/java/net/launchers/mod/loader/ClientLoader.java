package net.launchers.mod.loader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.launchers.mod.entity_renderer.LauncherBlockEntityRenderer;

import static net.launchers.mod.loader.LaunchersLoader.LAUNCHER_BLOCK_ENTITY;

public class ClientLoader implements ClientModInitializer
{
    //PistonBlockEntityRenderer
    @Override
    public void onInitializeClient()
    {
        BlockEntityRendererRegistry.INSTANCE.register(LAUNCHER_BLOCK_ENTITY, LauncherBlockEntityRenderer::new);
        //        BlockEntityRendererRegistry.INSTANCE.register(PW_LAUNCHER_BLOCK_ENTITY, PoweredLauncherBlockEntityRenderer::new);
        //        BlockEntityRendererRegistry.INSTANCE.register(EX_LAUNCHER_BLOCK_ENTITY, ExtremeLauncherBlockEntityRenderer::new);
    }
}
