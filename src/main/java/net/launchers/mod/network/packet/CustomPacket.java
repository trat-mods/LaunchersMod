package net.launchers.mod.network.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.launchers.mod.loader.LaunchersLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public abstract class CustomPacket
{
    public static Identifier PACKET_ID = new Identifier(LaunchersLoader.MOD_ID, "cp");
    
    public CustomPacket()
    {
    }
    
    public void sendTo(PlayerEntity player)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf);
    }
    
    protected abstract void onReceive(PacketContext ctx);
    
    public abstract void write(PacketByteBuf buf);
}
