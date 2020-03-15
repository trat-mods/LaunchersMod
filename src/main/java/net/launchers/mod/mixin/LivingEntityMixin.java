package net.launchers.mod.mixin;

import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
    public LivingEntityMixin(EntityType<?> type, World world)
    {
        super(type, world);
    }
    
    @Inject(at = @At("HEAD"), method = "jump", cancellable = true)
    private void jump(CallbackInfo info)
    {
        if(!world.isClient)
        {
            BlockPos pos = new BlockPos(getX(), getY() - 1, getZ());
            if(world.getBlockState(pos).getBlock() instanceof AbstractLauncherBlock)
            {
                AbstractLauncherBlock launcherBlock = (AbstractLauncherBlock) world.getBlockState(pos).getBlock();
                List<LivingEntity> livingEntities = world.getNonSpectatingEntities(LivingEntity.class, (new Box(pos)).expand(0D, 1D, 0D));
                launcherBlock.launchEntities(world, pos, livingEntities);
                info.cancel();
            }
        }
    }
}