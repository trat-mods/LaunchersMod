package net.launchers.mod.entity_renderer;

import net.launchers.mod.entity.PoweredLauncherBlockEntity;
import net.launchers.mod.entity_renderer.abstraction.AbstractLauncherBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class PoweredLauncherBlockEntityRenderer extends AbstractLauncherBlockEntityRenderer<PoweredLauncherBlockEntity> {
    public PoweredLauncherBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }
}
