package net.launchers.mod.block.abstraction;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockEntity;
import net.launchers.mod.initializer.LMSounds;
import net.launchers.mod.loader.LMLoader;
import net.launchers.mod.network.NetworkHandler;
import net.launchers.mod.network.packet.UnboundedEntityVelocityPayload;
import net.launchers.mod.utils.MathUtils;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public abstract class AbstractLauncherBlock extends BlockWithEntity {
    public static final Identifier LAUNCH_SOUND = new Identifier(LMLoader.MOD_ID, "launcher_block_launch");
    public static final BooleanProperty TRIGGERED;
    public static final IntProperty MODELS = IntProperty.of("models", 0, 2);
    public static final DirectionProperty FACING;

    static {
        TRIGGERED = Properties.TRIGGERED;
        FACING = FacingBlock.FACING;
    }

    private final float launchForce = 1F;
    private final int maxStackable = 4;
    public float stackMultiplier;
    public float baseMultiplier;
    protected float stackPowerPercentage;

    //DropperBlock

    //TntEntity
    public AbstractLauncherBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(MODELS, 0).with(TRIGGERED, false).with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
        return Block.createCuboidShape(0F, 0F, 0F, 16F, 16F, 16F);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.handleFallDamage(fallDistance, 0.0F, world.getDamageSources().fall());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(MODELS, TRIGGERED, FACING);
    }

    public void launchEntities(World world, BlockPos pos, List<? extends Entity> entities) {
        if (!world.isClient) {
            if (entities.size() < 1) {
                return;
            }
            float force = launchForce * baseMultiplier;

            BlockState parentState = world.getBlockState(pos);
            Direction stackDirection = parentState.get(FACING).getOpposite();
            BlockPos currentPos = pos.offset(stackDirection);
            int currentIndex = 1;
            double multiplier = 1F;
            if (!stackDirection.equals(Direction.UP) && !stackDirection.equals(Direction.DOWN)) {
                multiplier *= 1.75F;
            }
            Block current;
            while (currentIndex < maxStackable && ((current = world.getBlockState(currentPos).getBlock()) instanceof AbstractLauncherBlock && world.getBlockState(currentPos).get(FACING).equals(parentState.get(FACING)))) {
                AbstractLauncherBlock launcherBlock = (AbstractLauncherBlock) current;
                multiplier += launcherBlock.stackMultiplier;
                currentPos = currentPos.offset(stackDirection);
                currentIndex++;
            }
            force *= multiplier;
            for (Entity entity : entities) {
                Vec3d initialVelocity = entity.getVelocity();
                Vec3d vectorForce = MathUtils.fromDirection(world.getBlockState(pos).get(AbstractLauncherBlock.FACING));
                Vec3d velocity = vectorForce.multiply(force).add(initialVelocity);
                entity.setVelocity(velocity);
                UnboundedEntityVelocityPayload packet = new UnboundedEntityVelocityPayload(velocity, entity.getId());
                NetworkHandler.sendToAll(packet, world.getServer().getPlayerManager());
            }
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
        AbstractLauncherBlockEntity launcherBlockEntity = (AbstractLauncherBlockEntity) world.getBlockEntity(pos);
        boolean isRecevingRedstonePower = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        boolean isTriggered = (Boolean) state.get(TRIGGERED);
        boolean isRetracted = launcherBlockEntity.launcherState == AbstractLauncherBlockEntity.LauncherState.RETRACTED;
        if (!isRetracted) return;
        if (isRecevingRedstonePower && !isTriggered) {
            scheduledTick(state, (ServerWorld) world, pos, new Random());
            //            world.getBlockTickScheduler().sc(pos, this, this.getTickRate(world));
            world.setBlockState(pos, (BlockState) state.with(TRIGGERED, true), 4);
        }
        else if (!isRecevingRedstonePower && isTriggered) {
            world.setBlockState(pos, (BlockState) state.with(TRIGGERED, false), 4);
        }
    }

    @Nullable
    @Override
    public abstract <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type);

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (canLaunch(world, pos)) {
            List<Entity> entities = world.getNonSpectatingEntities(Entity.class, new Box(pos.offset(state.get(FACING))));
            launchEntities(world, pos, entities);
            playLaunchSound(world, pos);
            ((AbstractLauncherBlockEntity) world.getBlockEntity(pos)).startExtending();
        }
    }

    protected void playLaunchSound(World world, BlockPos pos) {
        double d = (double) pos.getX() + 0.5D;
        double e = (double) pos.getY();
        double f = (double) pos.getZ() + 0.5D;
        world.getServer().getPlayerManager().sendToAll(createLaunchSoundPacket(d, e, f));
    }

    public PlaySoundS2CPacket createLaunchSoundPacket(double x, double y, double z) {
        return new PlaySoundS2CPacket(LMSounds.LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT, SoundCategory.BLOCKS, x, y, z, 1F, 1F, 0);
    }


    public int getTickRate(WorldView worldView) {
        return 1;
    }


    public boolean canLaunch(World world, BlockPos pos) {
        AbstractLauncherBlockEntity launcherBlockEntity = (AbstractLauncherBlockEntity) world.getBlockEntity(pos);
        BlockPos offset = pos.offset(world.getBlockState(pos).get(FACING));
        return (world.getBlockState(offset).isAir() || world.getBlockState(offset).getBlock().equals(Blocks.TRIPWIRE)) && launcherBlockEntity.launcherState == AbstractLauncherBlockEntity.LauncherState.RETRACTED;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction) state.get(FACING)));
    }

    @Nullable
    @Override
    public abstract BlockEntity createBlockEntity(BlockPos pos, BlockState state);
}
