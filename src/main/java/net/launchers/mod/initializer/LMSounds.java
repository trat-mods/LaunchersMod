package net.launchers.mod.initializer;

import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class LMSounds
{
    public static SoundEvent LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT = new SoundEvent(AbstractLauncherBlock.LAUNCH_SOUND);
    
    public static void initialize()
    {
        Registry.register(Registry.SOUND_EVENT, AbstractLauncherBlock.LAUNCH_SOUND, LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT);
    }
}
