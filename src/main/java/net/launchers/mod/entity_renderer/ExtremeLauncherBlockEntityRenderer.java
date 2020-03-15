package net.launchers.mod.entity_renderer;

import net.launchers.mod.entity.ExtremeLauncherBlockEntity;
import net.launchers.mod.entity_renderer.abstraction.AbstractLauncherBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;

public class ExtremeLauncherBlockEntityRenderer extends AbstractLauncherBlockEntityRenderer<ExtremeLauncherBlockEntity>
{
    public ExtremeLauncherBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }
}
