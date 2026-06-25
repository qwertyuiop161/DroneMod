package com.drones;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModEntityModelLayers {
    public static final ModelLayerLocation DRONE = createMain("drone");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.DRONE, DroneModel::createBodyLayer);
    }
}
