package net.launchers.mod.initializer;

import net.launchers.mod.block.ExtremeLauncherBlock;
import net.launchers.mod.block.LauncherBlock;
import net.launchers.mod.block.PoweredLauncherBlock;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

public class LMBlock
{
    public static final Block LAUNCHER_BLOCK = new LauncherBlock();
    public static final Block POWERED_LAUNCHER_BLOCK = new PoweredLauncherBlock();
    public static final Block EXTREME_LAUNCHER_BLOCK = new ExtremeLauncherBlock();
    
    public static void initialize()
    {
        Registry.register(Registry.BLOCK, LauncherBlock.ID, LAUNCHER_BLOCK);
        Registry.register(Registry.BLOCK, PoweredLauncherBlock.ID, POWERED_LAUNCHER_BLOCK);
        Registry.register(Registry.BLOCK, ExtremeLauncherBlock.ID, EXTREME_LAUNCHER_BLOCK);
    }
}
