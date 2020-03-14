package net.launchers.mod.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.LauncherBlockEntity;
import net.launchers.mod.loader.LaunchersLoader;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;

public class LauncherBlock extends AbstractLauncherBlock
{
    public static final Identifier ID = new Identifier(LaunchersLoader.MOD_ID, "launcher_block");
    
    public LauncherBlock()
    {
        super(FabricBlockSettings.of(Material.METAL).breakByHand((true)).dynamicBounds().sounds(BlockSoundGroup.METAL).nonOpaque().strength(0.8F, 0.5F).build());
    }
    
    @Override
    public BlockEntity createBlockEntity(BlockView view)
    {
        return new LauncherBlockEntity();
    }
}