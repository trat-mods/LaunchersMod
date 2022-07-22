package net.launchers.mod.network.packet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

public abstract class CustomPacket {


    public CustomPacket() {
    }

    public abstract void sendTo(PlayerEntity player);

    protected abstract void onReceive(MinecraftClient ctx);

    public abstract void write(PacketByteBuf buf);
}
