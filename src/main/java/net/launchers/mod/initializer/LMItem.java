package net.launchers.mod.initializer;

import net.launchers.mod.block.ExtremeLauncherBlock;
import net.launchers.mod.block.LauncherBlock;
import net.launchers.mod.block.PoweredLauncherBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

import static net.launchers.mod.initializer.LMBlock.*;

public final class LMItem
{
    public static void initialize()
    {
        Registry.register(Registry.ITEM, ExtremeLauncherBlock.ID, new BlockItem(EXTREME_LAUNCHER_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, PoweredLauncherBlock.ID, new BlockItem(POWERED_LAUNCHER_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, LauncherBlock.ID, new BlockItem(LAUNCHER_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
    }
}
