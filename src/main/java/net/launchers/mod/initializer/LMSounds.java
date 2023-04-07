package net.launchers.mod.initializer;

import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

public final class LMSounds {
    public static RegistryEntry<SoundEvent> LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT = RegistryEntry.of(SoundEvent.of(AbstractLauncherBlock.LAUNCH_SOUND));

    public static void initialize() {
        Registry.register(Registries.SOUND_EVENT, AbstractLauncherBlock.LAUNCH_SOUND, LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT.value());
    }
}
