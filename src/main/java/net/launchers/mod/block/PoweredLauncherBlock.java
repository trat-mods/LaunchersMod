package net.launchers.mod.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.PoweredLauncherBlockEntity;
import net.launchers.mod.loader.LaunchersLoader;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;

public class PoweredLauncherBlock extends AbstractLauncherBlock
{
    public static final Identifier ID = new Identifier(LaunchersLoader.MOD_ID, "powered_launcher_block");
    
    public PoweredLauncherBlock()
    {
        super(FabricBlockSettings.of(Material.METAL).breakByHand((true)).strength(1F, 0.85F).sounds(BlockSoundGroup.METAL).nonOpaque().dynamicBounds().build());
        baseMultiplier = 2F;
        stackPowerPercentage = 0.325F;
        stackMultiplier = baseMultiplier * stackPowerPercentage;
    }
    
    @Override
    public BlockEntity createBlockEntity(BlockView view)
    {
        return new PoweredLauncherBlockEntity();
    }
}