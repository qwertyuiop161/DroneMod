package com.drones;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class DroneEntityModel extends EntityModel<DroneEntityRenderState> {

    private final ModelPart head;

    public DroneEntityModel(ModelPart root) {
        super(root);
        this.head=root.getChild(PartNames.HEAD);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition root = modelData.getRoot();
	    root.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(36, 0).addBox(-3, -6, -3, 6, 6, 6), PartPose.offset(0, 2, 0));
        return LayerDefinition.create(modelData, 64, 32);
    }
}