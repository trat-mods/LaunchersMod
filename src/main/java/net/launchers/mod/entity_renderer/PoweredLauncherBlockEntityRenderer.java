package net.launchers.mod.entity_renderer;

import net.launchers.mod.entity.PoweredLauncherBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class PoweredLauncherBlockEntityRenderer extends BlockEntityRenderer<PoweredLauncherBlockEntity>
{
    public PoweredLauncherBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }
    
    @Override
    public void render(PoweredLauncherBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
    
    }
}
