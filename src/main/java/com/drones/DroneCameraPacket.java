package com.drones;

import java.util.UUID;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record DroneCameraPacket(boolean enter) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, "drone_camera");
    public static final CustomPacketPayload.Type<DroneCameraPacket> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, DroneCameraPacket> CODEC = StreamCodec
            .of((buf, packet) -> buf.writeBoolean(packet.enter), buf -> new DroneCameraPacket(buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(DroneCameraPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        context.server().execute(() -> {
            if (!packet.enter()) {
                player.setCamera(player);
                player.sendSystemMessage(Component.literal("[Drone] Camera OFF"));
                return;
            }
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
            player.setCamera(drone);
            player.sendSystemMessage(Component.literal("[Drone] Camera ON - Shift+right-click to exit"));
        });
    }
}
