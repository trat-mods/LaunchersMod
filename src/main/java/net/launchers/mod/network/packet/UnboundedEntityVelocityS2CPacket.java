package net.launchers.mod.network.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.launchers.mod.loader.LMLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class UnboundedEntityVelocityS2CPacket extends CustomPacket {
    public static Identifier PACKET_ID = new Identifier(LMLoader.MOD_ID, "unv_packet");
    private final Vec3d velocity;
    private final int entityId;

    public UnboundedEntityVelocityS2CPacket(int entityId, Vec3d velocity) {
        this.velocity = velocity;
        this.entityId = entityId;
    }

    public UnboundedEntityVelocityS2CPacket(int entityId, float x, float y, float z) {
        this(entityId, new Vec3d(x, y, z));
    }

    public UnboundedEntityVelocityS2CPacket(Entity entity, Vec3d velocity) {
        this(entity.getId(), velocity);
    }

    public UnboundedEntityVelocityS2CPacket(Entity entity, float x, float y, float z) {
        this(entity.getId(), new Vec3d(x, y, z));
    }

    public static UnboundedEntityVelocityS2CPacket read(PacketByteBuf buf) {
        int entityId = buf.readVarInt();
        Vec3d velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return new UnboundedEntityVelocityS2CPacket(entityId, velocity);
    }

    @Override
    public void sendTo(PlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf);
        ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_ID, buf);
    }

    public void onReceive(MinecraftClient ctx) {
        ctx.execute(() -> {
            Entity targetEntity = ctx.player.world.getEntityById(entityId);
            targetEntity.setVelocity(velocity);
        });
    }

    public void write(PacketByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeDouble(velocity.getX());
        buf.writeDouble(velocity.getY());
        buf.writeDouble(velocity.getZ());
    }
}
