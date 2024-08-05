package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockEntity;
import net.launchers.mod.initializer.LMEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ExtremeLauncherBlockEntity extends AbstractLauncherBlockEntity {
    public ExtremeLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(LMEntities.EX_LAUNCHER_BLOCK_ENTITY, pos, state);
    }
}
