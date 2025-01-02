package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockEntity;
import net.launchers.mod.initializer.LMEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class UltimateLauncherBlockEntity extends AbstractLauncherBlockEntity {
    public UltimateLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(LMEntities.UL_LAUNCHER_BLOCK_ENTITY, pos, state);
    }
}
