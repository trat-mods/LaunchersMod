package net.launchers.mod.initializer;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.launchers.mod.block.ExtremeLauncherBlock;
import net.launchers.mod.block.LauncherBlock;
import net.launchers.mod.block.PoweredLauncherBlock;
import net.launchers.mod.entity.ExtremeLauncherBlockEntity;
import net.launchers.mod.entity.LauncherBlockEntity;
import net.launchers.mod.entity.PoweredLauncherBlockEntity;
import net.launchers.mod.loader.LMLoader;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import static net.launchers.mod.initializer.LMBlock.*;

public final class LMEntities {
    public static BlockEntityType<LauncherBlockEntity> LAUNCHER_BLOCK_ENTITY;
    public static BlockEntityType<PoweredLauncherBlockEntity> PW_LAUNCHER_BLOCK_ENTITY;
    public static BlockEntityType<ExtremeLauncherBlockEntity> EX_LAUNCHER_BLOCK_ENTITY;

    public static void initialize() {
        LAUNCHER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, LauncherBlock.ID, FabricBlockEntityTypeBuilder.create(LauncherBlockEntity::new, LAUNCHER_BLOCK).build(null));
        PW_LAUNCHER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, LMLoader.MOD_ID + ":" + PoweredLauncherBlock.ID.getPath(),
                                                     FabricBlockEntityTypeBuilder.create(PoweredLauncherBlockEntity::new, POWERED_LAUNCHER_BLOCK).build(null));
        EX_LAUNCHER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, LMLoader.MOD_ID + ":" + ExtremeLauncherBlock.ID.getPath(),
                                                     FabricBlockEntityTypeBuilder.create(ExtremeLauncherBlockEntity::new, EXTREME_LAUNCHER_BLOCK).build(null));
    }
}
