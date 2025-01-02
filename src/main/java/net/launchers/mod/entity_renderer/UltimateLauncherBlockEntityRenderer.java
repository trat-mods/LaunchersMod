package net.launchers.mod.entity_renderer;

import net.launchers.mod.entity.UltimateLauncherBlockEntity;
import net.launchers.mod.entity_renderer.abstraction.AbstractLauncherBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class UltimateLauncherBlockEntityRenderer extends AbstractLauncherBlockEntityRenderer<UltimateLauncherBlockEntity> {
    public UltimateLauncherBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }
}
