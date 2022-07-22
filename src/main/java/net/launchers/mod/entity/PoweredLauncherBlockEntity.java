package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockEntity;
import net.launchers.mod.initializer.LMEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PoweredLauncherBlockEntity extends AbstractLauncherBlockEntity {
    public PoweredLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(LMEntities.PW_LAUNCHER_BLOCK_ENTITY, pos, state);
    }
}
