package net.launchers.mod.loader;

import net.fabricmc.api.ModInitializer;
import net.launchers.mod.block.ExtremeLauncherBlock;
import net.launchers.mod.block.LauncherBlock;
import net.launchers.mod.block.PoweredLauncherBlock;
import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.ExtremeLauncherBlockEntity;
import net.launchers.mod.entity.LauncherBlockEntity;
import net.launchers.mod.entity.PoweredLauncherBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class LaunchersLoader implements ModInitializer
{
    public static final String MOD_ID = "launchersmod";
    public static final Block LAUNCHER_BLOCK = new LauncherBlock();
    public static final Block POWERED_LAUNCHER_BLOCK = new PoweredLauncherBlock();
    public static final Block EXTREME_LAUNCHER_BLOCK = new ExtremeLauncherBlock();
    
    public static BlockEntityType<LauncherBlockEntity> LAUNCHER_BLOCK_ENTITY;
    public static BlockEntityType<PoweredLauncherBlockEntity> PW_LAUNCHER_BLOCK_ENTITY;
    public static BlockEntityType<ExtremeLauncherBlockEntity> EX_LAUNCHER_BLOCK_ENTITY;
    
    public static SoundEvent LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT = new SoundEvent(AbstractLauncherBlock.LAUNCH_SOUND);
    
    @Override public void onInitialize()
    {
        Registry.register(Registry.BLOCK, LauncherBlock.ID, LAUNCHER_BLOCK);
        Registry.register(Registry.BLOCK, PoweredLauncherBlock.ID, POWERED_LAUNCHER_BLOCK);
        Registry.register(Registry.BLOCK, ExtremeLauncherBlock.ID, EXTREME_LAUNCHER_BLOCK);
        Registry.register(Registry.ITEM, ExtremeLauncherBlock.ID, new BlockItem(EXTREME_LAUNCHER_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, PoweredLauncherBlock.ID, new BlockItem(POWERED_LAUNCHER_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, LauncherBlock.ID, new BlockItem(LAUNCHER_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
        LAUNCHER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, LauncherBlock.ID, BlockEntityType.Builder.create(LauncherBlockEntity::new, LAUNCHER_BLOCK).build(null));
        PW_LAUNCHER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":" + PoweredLauncherBlock.ID.getPath(), BlockEntityType.Builder.create(PoweredLauncherBlockEntity::new, POWERED_LAUNCHER_BLOCK).build(null));
        EX_LAUNCHER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":" + ExtremeLauncherBlock.ID.getPath(), BlockEntityType.Builder.create(ExtremeLauncherBlockEntity::new, EXTREME_LAUNCHER_BLOCK).build(null));
        Registry.register(Registry.SOUND_EVENT, AbstractLauncherBlock.LAUNCH_SOUND, LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT);
    }
}
