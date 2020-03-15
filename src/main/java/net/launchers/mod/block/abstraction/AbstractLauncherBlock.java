package net.launchers.mod.block.abstraction;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockEntity;
import net.launchers.mod.loader.LaunchersLoader;
import net.launchers.mod.network.LaunchersNetworkHandler;
import net.launchers.mod.network.packet.UnboundedPlayerVelocityS2CPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.PlaySoundS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractLauncherBlock extends Block implements BlockEntityProvider
{
    public static final Identifier LAUNCH_SOUND = new Identifier(LaunchersLoader.MOD_ID, "launcher_block_launch");
    
    public static final IntProperty MODELS = IntProperty.of("models", 0, 2);
    private float launchForce = 1F;
    //PistonHeadBlock
    private int maxStackable = 4;
    protected float stackPowerPercentage;
    protected float stackMultiplier;
    protected float baseMultiplier;
    
    //TntEntity
    public AbstractLauncherBlock(Settings settings)
    {
        super(settings);
        this.setDefaultState((BlockState) ((BlockState) ((BlockState) this.stateManager.getDefaultState()).with(MODELS, 0)));
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ePos)
    {
        AbstractLauncherBlockEntity entity = (AbstractLauncherBlockEntity) view.getBlockEntity(pos);
        return entity != null ? entity.getCollisionShape() : Block.createCuboidShape(0F, 0F, 0F, 16F, 16F, 16F);
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
        if(!world.isClient)
        {
            if(entities.size() < 1)
            {
                return;
            }
            float force = launchForce * baseMultiplier;
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
            for(LivingEntity entity : entities)
            {
                UnboundedPlayerVelocityS2CPacket packet = new UnboundedPlayerVelocityS2CPacket(entity, 0F, force, 0F);
                LaunchersNetworkHandler.sendToAll(packet, world.getServer().getPlayerManager());
            }
        }
    }
    
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved)
    {
        AbstractLauncherBlockEntity launcherBlockEntity = (AbstractLauncherBlockEntity) world.getBlockEntity(pos);
        if(world.isReceivingRedstonePower(pos) && launcherBlockEntity.launcherState == AbstractLauncherBlockEntity.LauncherState.RETRACTED)
        {
            world.getBlockTickScheduler().schedule(pos, this, getTickRate(world));
        }
        super.neighborUpdate(state, world, pos, block, neighborPos, moved);
    }
    
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        if(canLaunch(world, pos))
        {
            List<LivingEntity> livingEntities = world.getNonSpectatingEntities(LivingEntity.class, (new Box(pos)).expand(0D, 1D, 0D));
            launchEntities(world, pos, livingEntities);
            playLaunchSound(world, pos);
            ((AbstractLauncherBlockEntity) world.getBlockEntity(pos)).startExtending();
        }
    }
    
    private void playLaunchSound(World world, BlockPos pos)
    {
        double d = (double) pos.getX() + 0.5D;
        double e = (double) pos.getY();
        double f = (double) pos.getZ() + 0.5D;
        PlaySoundS2CPacket soundS2CPacket = new PlaySoundS2CPacket(LaunchersLoader.LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT, SoundCategory.BLOCKS, d, e, f, 1.25F, 0.9F);
        world.getServer().getPlayerManager().sendToAll(soundS2CPacket);
    }
    
    @Override
    public int getTickRate(WorldView worldView)
    {
        return 2;
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
        return world.getBlockState(pos.up()).getBlock() == Blocks.AIR && launcherBlockEntity.launcherState == AbstractLauncherBlockEntity.LauncherState.RETRACTED;
    }
    
    @Override
    public abstract BlockEntity createBlockEntity(BlockView view);
}
