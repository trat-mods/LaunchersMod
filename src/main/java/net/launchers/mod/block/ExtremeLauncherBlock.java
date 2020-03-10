package net.launchers.mod.block;

import net.launchers.mod.loader.LaunchersLoader;
import net.minecraft.util.Identifier;

public class ExtremeLauncherBlock extends LauncherBlock
{
    public static final Identifier ID = new Identifier(LaunchersLoader.MOD_ID, "extreme_launcher_block");
    
    public ExtremeLauncherBlock()
    {
        super();
        baseMultiplier = 3F;
        stackPowerPercentage = 0.325F;
        stackMultiplier = baseMultiplier * stackPowerPercentage;
    }
}
