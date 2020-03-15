package net.launchers.mod.entity_renderer;

import net.launchers.mod.entity.PoweredLauncherBlockEntity;
import net.launchers.mod.entity_renderer.abstraction.AbstractLauncherBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;

public class PoweredLauncherBlockEntityRenderer extends AbstractLauncherBlockEntityRenderer<PoweredLauncherBlockEntity>
{
    public PoweredLauncherBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }
}
