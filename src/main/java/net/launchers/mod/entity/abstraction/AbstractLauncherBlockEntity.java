package net.launchers.mod.entity.abstraction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractLauncherBlockEntity extends BlockEntity {
    private final VoxelShape RETRACTED_BASE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape EXTENDED_BASE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    private final VoxelShape SHORT_EXTENDER_SHAPE = Block.createCuboidShape(6.0D, 2.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    private final VoxelShape LONG_EXTENDER_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private final VoxelShape HEAD_SHAPE = Block.createCuboidShape(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final float extensionStride = 1F; // 1/stride ticks per move
    private final float retractingStride = extensionStride / 4;
    private final int retractingDelay = 2;
    public LauncherState[] states;
    public LauncherState launcherState;
    protected int currentTick = 0;
    private float maxExtendCoefficient;
    private float progress;
    private float lastProgress;
    private boolean extending = true; // true if its extending, false if retracting

    public AbstractLauncherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        states = LauncherState.values();
        launcherState = LauncherState.RETRACTED;
    }

    public static void tick(World world, BlockPos pos, BlockState state, AbstractLauncherBlockEntity be) {
        switch (be.launcherState) {
            case EXTENDED:
                be.currentTick++;
                if (be.currentTick >= be.retractingDelay) {
                    be.currentTick = 0;
                    be.startRetracting();
                }
                break;
            case RETRACTED:
                break;
            case MOVING:
                be.lastProgress = be.progress;
                if (be.extending) {
                    if (be.lastProgress >= 1.0F) {
                        be.launcherState = LauncherState.EXTENDED;
                        be.lastProgress = 1F;
                    }
                    else {
                        be.progress += be.extensionStride;
                        if (be.progress >= 1.0F) {
                            be.progress = 1.0F;
                        }
                    }
                }
                else {
                    if (be.lastProgress <= 0F) {
                        be.launcherState = LauncherState.RETRACTED;
                        be.lastProgress = 0F;
                    }
                    else {
                        be.progress -= be.retractingStride;
                        if (be.progress <= 0F) {
                            be.progress = 0F;
                        }
                    }
                }
                break;
        }
    }

    public boolean isRetracted() {
        return launcherState == LauncherState.RETRACTED;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public VoxelShape getCollisionShape() {
        VoxelShape baseShape = null;
        if (isRetracted()) {
            baseShape = RETRACTED_BASE_SHAPE;
            return baseShape;
        }
        else {
            baseShape = EXTENDED_BASE_SHAPE;
            VoxelShape extenderShape = null;
            if (progress < 0.35) {
                extenderShape = VoxelShapes.union(baseShape, SHORT_EXTENDER_SHAPE.offset(0, progress - 0.25, 0));
            }
            else {
                extenderShape = VoxelShapes.union(baseShape, LONG_EXTENDER_SHAPE.offset(0, progress - 0.25F, 0));
            }
            return VoxelShapes.union(extenderShape, HEAD_SHAPE.offset(0, progress, 0));
        }
    }

    @Environment(EnvType.CLIENT)
    public float getDeltaProgress(float tickDelta) {
        if (tickDelta > 1.0F) {
            tickDelta = 1.0F;
        }
        return MathHelper.lerp(tickDelta, this.lastProgress, this.progress);
    }

    public void startExtending() {
        extending = true;
        launcherState = LauncherState.MOVING;
        progress = 0;
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NOTIFY_LISTENERS);
    }

    public void startRetracting() {
        extending = false;
        launcherState = LauncherState.MOVING;
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NOTIFY_LISTENERS);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.currentTick = nbt.getInt("currentTick");
        this.progress = nbt.getFloat("progress");
        this.lastProgress = this.progress;
        this.extending = nbt.getBoolean("extending");
        this.launcherState = states[nbt.getInt("launcherState")];
    }


    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("currentTick", currentTick);
        nbt.putFloat("progress", this.lastProgress);
        nbt.putBoolean("extending", this.extending);
        nbt.putInt("launcherState", launcherState.ordinal());
        super.writeNbt(nbt);
    }


    public enum LauncherState {EXTENDED, RETRACTED, MOVING}
}
