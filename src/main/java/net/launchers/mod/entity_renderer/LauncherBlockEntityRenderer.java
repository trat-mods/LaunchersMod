package net.launchers.mod.entity_renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.launchers.mod.entity.LauncherBlockEntity;
import net.launchers.mod.entity_renderer.abstraction.AbstractLauncherBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

@Environment(EnvType.CLIENT)
public class LauncherBlockEntityRenderer extends AbstractLauncherBlockEntityRenderer<LauncherBlockEntity> {
    public LauncherBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }
}
