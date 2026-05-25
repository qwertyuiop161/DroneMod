package com.drones;

import java.util.UUID;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record DroneControlPacket(float dx, float dy, float dz, float yaw, float pitch, boolean exit)
        implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("craftable-drones", "drone_control");
    public static final CustomPacketPayload.Type<DroneControlPacket> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, DroneControlPacket> CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeFloat(packet.dx);
                buf.writeFloat(packet.dy);
                buf.writeFloat(packet.dz);
                buf.writeFloat(packet.yaw);
                buf.writeFloat(packet.pitch);
                buf.writeBoolean(packet.exit);
            },
            buf -> new DroneControlPacket(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readFloat(), buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(DroneControlPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        context.server().execute(() -> {
            UUID linkedUUID = null;
            int size = player.getInventory().getContainerSize();
            for (int i = 0; i < size; i++) {
                UUID u = player.getInventory().getItem(i).get(ModDataComponentTypes.LINKED_DRONE_UUID);
                if (u != null) {
                    linkedUUID = u;
                    break;
                }
            }
            if (linkedUUID == null)
                return;
            Entity entity = player.level().getEntity(linkedUUID);
            if (!(entity instanceof DroneEntity drone))
                return;
            if (!drone.isLinked()) {
                drone.setControlled(false);
                return;
            }
            if (packet.exit()) {
                drone.setControlled(false);
                return;
            }
            if (!drone.hasBattery()) {
                drone.setControlled(false);
                return;
            }
            drone.setControlled(true);
            Vec3 movement = new Vec3(packet.dx(), packet.dy(), packet.dz()).scale(0.3);
            Vec3 current = drone.position();
            Vec3 target = current.add(movement);
            if (drone.level().noCollision(drone, drone.getBoundingBox().move(movement))) {
                drone.setPos(target.x, target.y, target.z);
            }
            drone.setDeltaMovement(Vec3.ZERO);
            drone.setYRot(packet.yaw);
            drone.setXRot(packet.pitch);
            drone.yRotO = packet.yaw;
            drone.xRotO = packet.pitch;
        });
    }

}
