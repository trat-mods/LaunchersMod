package net.launchers.mod.block;

import net.launchers.mod.loader.LaunchersLoader;
import net.minecraft.util.Identifier;

public class PoweredLauncherBlock extends LauncherBlock
{
    public static final Identifier ID = new Identifier(LaunchersLoader.MOD_ID, "powered_launcher_block");
    
    public PoweredLauncherBlock()
    {
        super();
        baseMultiplier = 2F;
        stackPowerPercentage = 0.325F;
        stackMultiplier = baseMultiplier * stackPowerPercentage;
    }
}