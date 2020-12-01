package net.launchers.mod.block.abstraction;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockEntity;
import net.launchers.mod.initializer.LMSounds;
import net.launchers.mod.loader.LMLoader;
import net.launchers.mod.network.LaunchersNetworkHandler;
import net.launchers.mod.network.packet.UnboundedEntityVelocityS2CPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.List;
import java.util.Random;

public abstract class AbstractLauncherBlock extends Block implements BlockEntityProvider
{
    public static final Identifier LAUNCH_SOUND = new Identifier(LMLoader.MOD_ID, "launcher_block_launch");
    public static final BooleanProperty TRIGGERED;
    public static final IntProperty MODELS = IntProperty.of("models", 0, 2);
    private float launchForce = 1F;
    private int maxStackable = 4;
    protected float stackPowerPercentage;
    public float stackMultiplier;
    public float baseMultiplier;
    
    static
    {
        TRIGGERED = Properties.TRIGGERED;
    }
    
    //DropperBlock
    
    //TntEntity
    public AbstractLauncherBlock(Settings settings)
    {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(MODELS, 0).with(TRIGGERED, false));
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ePos)
    {
        return Block.createCuboidShape(0F, 0F, 0F, 16F, 16F, 16F);
    }
    
    @Override
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance)
    {
        entity.handleFallDamage(distance, 0.0F);
    }
    
    @Override protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(MODELS, TRIGGERED);
    }
    
    public void launchEntities(World world, BlockPos pos, List<? extends Entity> entities)
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
            for(Entity entity : entities)
            {
                System.out.println(force);
                entity.setVelocity(new Vec3d(0F, force, 0F));
                UnboundedEntityVelocityS2CPacket packet = new UnboundedEntityVelocityS2CPacket(entity.getEntityId(), 0F, force, 0F);
                LaunchersNetworkHandler.sendToAll(packet, world.getServer().getPlayerManager());
            }
        }
    }
    
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved)
    {
        AbstractLauncherBlockEntity launcherBlockEntity = (AbstractLauncherBlockEntity) world.getBlockEntity(pos);
        boolean isRecevingRedstonePower = world.isReceivingRedstonePower(pos);
        boolean isTriggered = (Boolean) state.get(TRIGGERED);
        boolean isRetracted = launcherBlockEntity.launcherState == AbstractLauncherBlockEntity.LauncherState.RETRACTED;
        if(!isRetracted) return;
        if(isRecevingRedstonePower && !isTriggered)
        {
            world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
            world.setBlockState(pos, (BlockState) state.with(TRIGGERED, true), 4);
        }
        else if(!isRecevingRedstonePower && isTriggered)
        {
            world.setBlockState(pos, (BlockState) state.with(TRIGGERED, false), 4);
        }
    }
    
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        if(canLaunch(world, pos))
        {
            List<Entity> livingEntities = world.getNonSpectatingEntities(LivingEntity.class, (new Box(pos)).expand(0.15D, 1.25D, 0.15D));
            List<Entity> entities = world.getNonSpectatingEntities(ItemEntity.class, (new Box(pos)).expand(0.15D, 1.25D, 0.15D));
            livingEntities.addAll(entities);
            launchEntities(world, pos, livingEntities);
            playLaunchSound(world, pos);
            ((AbstractLauncherBlockEntity) world.getBlockEntity(pos)).startExtending();
        }
    }
    
    protected void playLaunchSound(World world, BlockPos pos)
    {
        double d = (double) pos.getX() + 0.5D;
        double e = (double) pos.getY();
        double f = (double) pos.getZ() + 0.5D;
        world.getServer().getPlayerManager().sendToAll(createLaunchSoundPacket(d, e, f));
    }
    
    public PlaySoundS2CPacket createLaunchSoundPacket(double x, double y, double z)
    {
        return new PlaySoundS2CPacket(LMSounds.LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT, SoundCategory.BLOCKS, x, y, z, 1F, 1F);
    }
    
    @Override
    public int getTickRate(WorldView worldView)
    {
        return 2;
    }
    
    
    public boolean canLaunch(World world, BlockPos pos)
    {
        AbstractLauncherBlockEntity launcherBlockEntity = (AbstractLauncherBlockEntity) world.getBlockEntity(pos);
        BlockPos up = pos.up();
        return !world.getBlockState(up).isSimpleFullBlock(world, up) && launcherBlockEntity.launcherState == AbstractLauncherBlockEntity.LauncherState.RETRACTED;
    }
    
    @Override
    public abstract BlockEntity createBlockEntity(BlockView view);
}
