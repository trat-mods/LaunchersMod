package net.launchers.mod.network.packet;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

public class UnboundedPlayerVelocityS2CPacket extends CustomPacket
{
    private final Vec3d velocity;
    private final int entityId;
    
    public UnboundedPlayerVelocityS2CPacket(int entityId, Vec3d velocity)
    {
        this.velocity = velocity;
        this.entityId = entityId;
    }
    
    public UnboundedPlayerVelocityS2CPacket(int entityId, float x, float y, float z)
    {
        this(entityId, new Vec3d(x, y, z));
    }
    
    public UnboundedPlayerVelocityS2CPacket(Entity entity, Vec3d velocity)
    {
        this(entity.getEntityId(), velocity);
    }
    
    public UnboundedPlayerVelocityS2CPacket(Entity entity, float x, float y, float z)
    {
        this(entity.getEntityId(), new Vec3d(x, y, z));
    }
    
    public void onReceive(PacketContext ctx)
    {
        ctx.getTaskQueue().execute(() ->
        {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            Entity targetEntity = ctx.getPlayer().world.getEntityById(entityId);
            targetEntity.setVelocity(velocity);
            System.out.println("The target entity velocity has been set");
        });
    }
    
    public void write(PacketByteBuf buf)
    {
        buf.writeVarInt(entityId);
        buf.writeDouble(velocity.getX());
        buf.writeDouble(velocity.getY());
        buf.writeDouble(velocity.getZ());
    }
    
    public static UnboundedPlayerVelocityS2CPacket read(PacketByteBuf buf)
    {
        int entityId = buf.readVarInt();
        Vec3d velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return new UnboundedPlayerVelocityS2CPacket(entityId, velocity);
    }
}
