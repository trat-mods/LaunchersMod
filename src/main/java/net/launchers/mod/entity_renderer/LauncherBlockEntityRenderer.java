package net.launchers.mod.entity_renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.LauncherBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class LauncherBlockEntityRenderer extends BlockEntityRenderer<LauncherBlockEntity>
{
    private final BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();
    
    public LauncherBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }
    
    //    Blocks
    public void render(LauncherBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        World world = blockEntity.getWorld();
        BlockState entityState = blockEntity.getCachedState();
        matrices.push();
        float extension = blockEntity.getDeltaProgress(tickDelta);
        BakedModel model = null;
        BlockState blockState = blockEntity.getCachedState();
        if(extension < 0.35F)
        {
            model = manager.getModel(blockState.with(AbstractLauncherBlock.MODELS, 2));
        }
        else
        {
            model = manager.getModel(blockState.with(AbstractLauncherBlock.MODELS, 1));
        }
        //System.out.println(extension);
        matrices.translate(0, extension, 0);
        RenderLayer renderLayer = RenderLayers.getEntityBlockLayer(entityState);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        //BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(new Identifier(LaunchersLoader.MOD_ID, "launcher_block_head"), "head"));
        this.manager.getModelRenderer().render(world, model, entityState, blockEntity.getPos().up(), matrices, vertexConsumer, true, new Random(), 4, overlay);
        matrices.pop();
    }
}
