package com.drones;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drones.item.ModItems;

public class CraftableDrones implements ModInitializer {
	public static final String MOD_ID = "craftable-drones";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModEntityModelLayers.registerModelLayers();
		FabricDefaultAttributeRegistry.register(ModEntityTypes.DRONE, DroneEntity.createCubeAttributes());
		ModDataComponentTypes.register();
		EntityRenderers.register(ModEntityTypes.DRONE, DroneEntityRenderer::new);
		PayloadTypeRegistry.serverboundPlay().register(DroneControlPacket.TYPE, DroneControlPacket.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(DroneControlPacket.TYPE, DroneControlPacket::handle);
		PayloadTypeRegistry.serverboundPlay().register(DroneCameraPacket.TYPE, DroneCameraPacket.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(DroneCameraPacket.TYPE, DroneCameraPacket::handle);
	}
}