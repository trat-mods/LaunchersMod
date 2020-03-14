package net.launchers.mod.entity.abstraction;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public abstract class AbstractLauncherBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable
{
    private final VoxelShape RETRACTED_BASE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape EXTENDED_BASE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    private final VoxelShape SHORT_EXTENDER_SHAPE = Block.createCuboidShape(6.0D, 2.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    private final VoxelShape LONG_EXTENDER_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private final VoxelShape HEAD_SHAPE = Block.createCuboidShape(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    
    public enum LauncherState
    {EXTENDED, RETRACTED, MOVING}
    
    public LauncherState[] states;
    protected float extensionStride = 1F; // 1/stride ticks per move
    protected float retractingStride = extensionStride / 4;
    protected int retractingDelay = 200;
    
    protected float progress;
    protected float lastProgress;
    protected boolean extending = true; // true if its extending, false if retracting
    
    protected int currentTick = 0;
    
    public LauncherState launcherState;
    
    public AbstractLauncherBlockEntity(BlockEntityType<?> type)
    {
        super(type);
        states = LauncherState.values();
        launcherState = LauncherState.RETRACTED;
    }
    
    public boolean isRetracted()
    {
        return launcherState == LauncherState.RETRACTED;
    }
    
    public VoxelShape getCollisionShape()
    {
        VoxelShape baseShape = null;
        if(isRetracted())
        {
            baseShape = RETRACTED_BASE_SHAPE;
            return baseShape;
        }
        else
        {
            baseShape = EXTENDED_BASE_SHAPE;
            VoxelShape extenderShape = null;
            if(progress < 0.35)
            {
                extenderShape = VoxelShapes.union(baseShape, SHORT_EXTENDER_SHAPE.offset(0, progress - 0.25, 0));
            }
            else
            {
                extenderShape = VoxelShapes.union(baseShape, LONG_EXTENDER_SHAPE.offset(0, progress - 0.25F, 0));
            }
            return VoxelShapes.union(extenderShape, HEAD_SHAPE.offset(0, progress, 0));
        }
    }
    
    public float getDeltaProgress(float tickDelta)
    {
        if(tickDelta > 1.0F)
        {
            tickDelta = 1.0F;
        }
        return MathHelper.lerp(tickDelta, this.lastProgress, this.progress);
    }
    
    @Override public void tick()
    {
        switch(launcherState)
        {
            case EXTENDED:
                currentTick++;
                if(currentTick >= retractingDelay)
                {
                    currentTick = 0;
                    startRetracting();
                }
                break;
            case RETRACTED:
                break;
            case MOVING:
                this.lastProgress = this.progress;
                if(extending)
                {
                    if(this.lastProgress >= 1.0F)
                    {
                        launcherState = LauncherState.EXTENDED;
                        this.lastProgress = 1F;
                    }
                    else
                    {
                        this.progress += extensionStride;
                        if(this.progress >= 1.0F)
                        {
                            this.progress = 1.0F;
                        }
                    }
                }
                else
                {
                    if(this.lastProgress <= 0F)
                    {
                        launcherState = LauncherState.RETRACTED;
                        lastProgress = 0F;
                    }
                    else
                    {
                        this.progress -= retractingStride;
                        if(this.progress <= 0F)
                        {
                            this.progress = 0F;
                        }
                    }
                }
                break;
        }
    }
    
    public void startExtending()
    {
        System.out.println("Starting");
        extending = true;
        launcherState = LauncherState.MOVING;
        progress = 0;
        sync();
    }
    
    public void startRetracting()
    {
        extending = false;
        launcherState = LauncherState.MOVING;
        if(!world.isClient)
        {
            sync();
        }
    }
    
    public void fromTag(CompoundTag tag)
    {
        super.fromTag(tag);
        this.currentTick = tag.getInt("currentTick");
        this.progress = tag.getFloat("progress");
        this.lastProgress = this.progress;
        this.extending = tag.getBoolean("extending");
        this.launcherState = states[tag.getInt("launcherState")];
    }
    
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);
        tag.putInt("currentTick", currentTick);
        tag.putFloat("progress", this.lastProgress);
        tag.putBoolean("extending", this.extending);
        tag.putInt("launcherState", launcherState.ordinal());
        return tag;
    }
    
    @Override public void fromClientTag(CompoundTag compoundTag)
    {
        fromTag(compoundTag);
    }
    
    @Override public CompoundTag toClientTag(CompoundTag compoundTag)
    {
        return toTag(compoundTag);
    }
}
