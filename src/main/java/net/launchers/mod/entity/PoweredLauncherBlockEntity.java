package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockEntity;
import net.launchers.mod.loader.LaunchersLoader;

public class PoweredLauncherBlockEntity extends AbstractLauncherBlockEntity
{
    public PoweredLauncherBlockEntity()
    {
        super(LaunchersLoader.PW_LAUNCHER_BLOCK_ENTITY);
    }
}
