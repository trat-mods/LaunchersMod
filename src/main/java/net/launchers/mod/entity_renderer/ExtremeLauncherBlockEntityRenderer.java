package net.launchers.mod.entity_renderer;

import net.launchers.mod.entity.ExtremeLauncherBlockEntity;
import net.launchers.mod.entity_renderer.abstraction.AbstractLauncherBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class ExtremeLauncherBlockEntityRenderer extends AbstractLauncherBlockEntityRenderer<ExtremeLauncherBlockEntity> {
    public ExtremeLauncherBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }
}
