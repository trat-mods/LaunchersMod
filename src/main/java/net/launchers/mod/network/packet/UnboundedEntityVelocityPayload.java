package net.launchers.mod.network.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public record UnboundedEntityVelocityPayload(Vec3d velocity, int entityId) implements CustomPayload {
    public static final CustomPayload.Id<UnboundedEntityVelocityPayload> ID = CustomPayload.id("lm:packet");
    public static final PacketCodec<PacketByteBuf, UnboundedEntityVelocityPayload> CODEC = PacketCodec.of(UnboundedEntityVelocityPayload::write, UnboundedEntityVelocityPayload::read);

    public static UnboundedEntityVelocityPayload read(PacketByteBuf buf) {
        int entityId = buf.readVarInt();
        Vec3d velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return new UnboundedEntityVelocityPayload(velocity, entityId);
    }

    public void sendTo(PlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf);
        ServerPlayNetworking.send((ServerPlayerEntity) player, this);
        //ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf);
    }

    public void onReceive(MinecraftClient ctx) {
        ctx.execute(() -> {
            Entity targetEntity = ctx.player.getWorld().getEntityById(entityId);
            targetEntity.setVelocity(velocity);
        });
    }

    public void write(PacketByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeDouble(velocity.getX());
        buf.writeDouble(velocity.getY());
        buf.writeDouble(velocity.getZ());
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
