package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockEntity;
import net.launchers.mod.loader.LaunchersLoader;
import net.minecraft.nbt.CompoundTag;

public class ExtremeLauncherBlockEntity extends AbstractLauncherBlockEntity
{
    public ExtremeLauncherBlockEntity()
    {
        super(LaunchersLoader.EX_LAUNCHER_BLOCK_ENTITY);
    }
}
