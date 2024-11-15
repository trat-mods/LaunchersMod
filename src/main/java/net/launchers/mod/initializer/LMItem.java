package net.launchers.mod.initializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.launchers.mod.block.ExtremeLauncherBlock;
import net.launchers.mod.block.LauncherBlock;
import net.launchers.mod.block.PoweredLauncherBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static net.launchers.mod.initializer.LMBlock.*;

public final class LMItem {
    public static void initialize() {
        var exLauncher = Registry.register(Registries.ITEM, ExtremeLauncherBlock.ID,
                                           new BlockItem(EXTREME_LAUNCHER_BLOCK, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, ExtremeLauncherBlock.ID))));
        var powLauncher = Registry.register(Registries.ITEM, PoweredLauncherBlock.ID,
                                            new BlockItem(POWERED_LAUNCHER_BLOCK, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, PoweredLauncherBlock.ID))));
        var launcher = Registry.register(Registries.ITEM, LauncherBlock.ID,
                                         new BlockItem(LAUNCHER_BLOCK, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, LauncherBlock.ID))));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(launcher));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(powLauncher));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(exLauncher));
    }
}
