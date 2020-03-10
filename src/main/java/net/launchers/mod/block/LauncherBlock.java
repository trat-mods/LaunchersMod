package net.launchers.mod.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.launchers.mod.loader.LaunchersLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LauncherBlock extends Block
{
    public static final Identifier ID = new Identifier(LaunchersLoader.MOD_ID, "launcher_block");
    
    private double launchForce = 1F;
    
    private int maxStackable = 4;
    protected double stackPowerPercentage = 0.325F;
    protected double stackMultiplier = 0.325;
    protected double baseMultiplier = 1F;
    
    public LauncherBlock()
    {
        super(FabricBlockSettings.of(Material.METAL).breakByHand((true)).sounds(BlockSoundGroup.METAL).strength(0.8F, 0.5F).build());
    }
    
    @Override
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance)
    {
        entity.handleFallDamage(distance, 0.0F);
    }
    
    public void launchLivingEntity(World world, BlockPos pos, LivingEntity entity)
    {
        double force = launchForce * baseMultiplier;
        BlockPos currentPos = pos.down();
        int currentIndex = 1;
        
        double multiplier = 1F;
        Block current;
        while(currentIndex < maxStackable && (current = world.getBlockState(currentPos).getBlock()) instanceof LauncherBlock)
        {
            LauncherBlock launcherBlock = (LauncherBlock) current;
            multiplier += launcherBlock.stackMultiplier;
            currentPos = currentPos.down();
            currentIndex++;
        }
        
        force *= multiplier;
        //System.out.println("F: " + force + ", SM: " + multiplier+", Index: " +currentIndex);
        entity.setVelocity(0F, force, 0F);
        return;
    }
}