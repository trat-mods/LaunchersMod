package net.launchers.mod.block.abstraction;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.EntityVelocityUpdateS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class AbstractLauncherBlock extends Block implements BlockEntityProvider
{
    public static final IntProperty MODELS = IntProperty.of("models", 0, 2);
    private double launchForce = 1F;
    //PistonHeadBlock
    private int maxStackable = 4;
    protected double stackPowerPercentage = 0.325F;
    protected double stackMultiplier = 0.325;
    protected double baseMultiplier = 1F;
    
    public AbstractLauncherBlock(Settings settings)
    {
        super(settings);
        this.setDefaultState((BlockState) ((BlockState) ((BlockState) this.stateManager.getDefaultState()).with(MODELS, 0)));
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ePos)
    {
        AbstractLauncherBlockEntity entity = (AbstractLauncherBlockEntity) view.getBlockEntity(pos);
        return entity != null ? entity.getCollisionShape() : VoxelShapes.empty();
    }
    
    @Override
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance)
    {
        entity.handleFallDamage(distance, 0.0F);
    }
    
    @Override protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(MODELS);
    }
    
    public void launchEntities(World world, BlockPos pos, List<LivingEntity> entities)
    {
        if(world.isClient) return;
        if(entities.size() < 1)
        {
            return;
        }
        double force = launchForce * baseMultiplier;
        BlockPos currentPos = pos.down();
        int currentIndex = 1;
        double multiplier = 1F;
        Block current;
        while(currentIndex < maxStackable && (current = world.getBlockState(currentPos).getBlock()) instanceof AbstractLauncherBlock)
        {
            AbstractLauncherBlock launcherBlock = (AbstractLauncherBlock) current;
            multiplier += launcherBlock.stackMultiplier;
            currentPos = currentPos.down();
            currentIndex++;
        }
        force *= multiplier;
        System.out.println("F: " + force + ", SM: " + multiplier + ", Index: " + currentIndex);
        for(LivingEntity entity : entities)
        {
            System.out.println("Setting velocity");
            entity.setVelocity(0F, force, 0F);
            Objects.requireNonNull(world.getServer()).getPlayerManager().sendToAll(new EntityVelocityUpdateS2CPacket(entity));
        }
    }
    
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved)
    {
        AbstractLauncherBlockEntity launcherBlockEntity = (AbstractLauncherBlockEntity) world.getBlockEntity(pos);
        if(world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up()) && launcherBlockEntity.launcherState == AbstractLauncherBlockEntity.LauncherState.RETRACTED)
        {
            world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
        }
    }
    
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        if(canLaunch(world, pos))
        {
            List<LivingEntity> livingEntities = world.getNonSpectatingEntities(LivingEntity.class, (new Box(pos)).expand(0D, 1D, 0D));
            System.out.println("Launching redstone " + livingEntities.size());
            launchEntities(world, pos, livingEntities);
            ((AbstractLauncherBlockEntity) world.getBlockEntity(pos)).startExtending();
        }
    }
    
    public void launchSingleEntity(World world, BlockPos pos, LivingEntity entity)
    {
        if(canLaunch(world, pos))
        {
            List<LivingEntity> list = new ArrayList<>();
            list.add(entity);
            launchEntities(world, pos, list);
            ((AbstractLauncherBlockEntity) world.getBlockEntity(pos)).startExtending();
        }
    }
    
    public boolean canLaunch(World world, BlockPos pos)
    {
        AbstractLauncherBlockEntity launcherBlockEntity = (AbstractLauncherBlockEntity) world.getBlockEntity(pos);
        return world.getBlockState(pos.up()).getBlock() == Blocks.AIR &&
               launcherBlockEntity.launcherState == AbstractLauncherBlockEntity.LauncherState.RETRACTED;
    }
    
    @Override
    public abstract BlockEntity createBlockEntity(BlockView view);
}
