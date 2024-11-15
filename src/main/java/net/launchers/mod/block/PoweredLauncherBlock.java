package net.launchers.mod.block;

import com.mojang.serialization.MapCodec;
import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.PoweredLauncherBlockEntity;
import net.launchers.mod.initializer.LMEntities;
import net.launchers.mod.initializer.LMSounds;
import net.launchers.mod.loader.LMLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PoweredLauncherBlock extends AbstractLauncherBlock {
    public static final Identifier ID = Identifier.of(LMLoader.MOD_ID, "powered_launcher_block");

    public PoweredLauncherBlock() {
        super(Block.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, ID)).strength(1F, 0.85F).sounds(BlockSoundGroup.METAL).nonOpaque().dynamicBounds());
        baseMultiplier = 2.125F;
        stackPowerPercentage = 0.2975F;
        stackMultiplier = baseMultiplier * stackPowerPercentage;
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, LMEntities.PW_LAUNCHER_BLOCK_ENTITY, PoweredLauncherBlockEntity::tick);
    }

    @Override
    public PlaySoundS2CPacket createLaunchSoundPacket(double x, double y, double z) {
        return new PlaySoundS2CPacket(LMSounds.LAUNCHER_BLOCK_LAUNCH_SOUNDEVENT, SoundCategory.BLOCKS, x, y, z, 0.9F, 0.875F, 0);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PoweredLauncherBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }
}