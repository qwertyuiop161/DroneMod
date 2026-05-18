package com.drones;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class DroneEntityRenderer extends MobRenderer<DroneEntity, DroneEntityRenderState, DroneEntityModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, "textures/entity/drone.png");
    public DroneEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DroneEntityModel(context.bakeLayer(ModEntityModelLayers.DRONE)), 0.375f);
    }
    @Override
    public DroneEntityRenderState createRenderState() {
        return new DroneEntityRenderState();
    }
    @Override
    public Identifier getTextureLocation(DroneEntityRenderState state) {
        return TEXTURE;
    }
}
