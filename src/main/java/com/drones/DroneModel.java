package com.drones;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class DroneModel extends EntityModel<DroneEntityRenderState> {

    private final ModelPart propellers;
    private final ModelPart frontLeft;
    private final ModelPart backLeft;
    private final ModelPart backRight;
    private final ModelPart frontRight;
    private final ModelPart body;
    private final ModelPart connectingParts;

    public DroneModel(ModelPart root) {
        super(root);
        this.propellers = root.getChild("propellers");
        this.frontLeft = this.propellers.getChild("f_left");
        this.backLeft = this.propellers.getChild("b_left");
        this.backRight = this.propellers.getChild("b_right");
        this.frontRight = this.propellers.getChild("f_right");
        this.body = root.getChild("body");
        this.connectingParts = root.getChild("connecting_parts");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition propellers = partDefinition.addOrReplaceChild(
            "propellers",
            CubeListBuilder.create(),
            PartPose.offset(-1.0F, 24.0F, -1.0F)
        );

        propellers.addOrReplaceChild(
            "f_left",
            CubeListBuilder.create()
                .texOffs(40, 0).addBox(-4.0F, -10.0F, -4.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-5.0F, -10.0F, -5.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-6.0F, -10.0F, -6.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-7.0F, -10.0F, -7.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-8.0F, -10.0F, -8.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-9.0F, -10.0F, -9.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-10.0F, -10.0F, -10.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-11.0F, -10.0F, -11.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-12.0F, -10.0F, -12.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-13.0F, -10.0F, -13.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE),
            PartPose.offsetAndRotation(18.0F, 0.0F, 4.0F, 0.0F, 1.5708F, 0.0F)
        );

        propellers.addOrReplaceChild(
            "b_left",
            CubeListBuilder.create()
                .texOffs(40, 0).addBox(10.0F, -10.0F, -4.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(9.0F, -10.0F, -5.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(8.0F, -10.0F, -6.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(7.0F, -10.0F, -7.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(6.0F, -10.0F, -8.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(5.0F, -10.0F, -9.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(4.0F, -10.0F, -10.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(3.0F, -10.0F, -11.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(2.0F, -10.0F, -12.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(1.0F, -10.0F, -13.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE),
            PartPose.offset(4.0F, 0.0F, -2.0F)
        );

        propellers.addOrReplaceChild(
            "b_right",
            CubeListBuilder.create()
                .texOffs(40, 0).addBox(16.0F, -10.0F, -4.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(15.0F, -10.0F, -5.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(14.0F, -10.0F, -6.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(13.0F, -10.0F, -7.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(12.0F, -10.0F, -8.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(11.0F, -10.0F, -9.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(10.0F, -10.0F, -10.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(9.0F, -10.0F, -11.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(8.0F, -10.0F, -12.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(7.0F, -10.0F, -13.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE),
            PartPose.offsetAndRotation(-2.0F, 0.0F, 4.0F, 0.0F, 1.5708F, 0.0F)
        );

        propellers.addOrReplaceChild(
            "f_right",
            CubeListBuilder.create()
                .texOffs(40, 0).addBox(4.0F, -2.0F, 2.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(3.0F, -2.0F, 1.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(2.0F, -2.0F, 0.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(0.0F, -2.0F, -2.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-1.0F, -2.0F, -3.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-2.0F, -2.0F, -4.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-3.0F, -2.0F, -5.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-4.0F, -2.0F, -6.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                .texOffs(40, 0).addBox(-5.0F, -2.0F, -7.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE),
            PartPose.offset(-10.0F, -8.0F, 12.0F)
        );

        PartDefinition body = partDefinition.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5.0F, -8.0F, -10.0F, 10.0F, 8.0F, 20.0F, CubeDeformation.NONE),
            PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        PartDefinition connectingParts = partDefinition.addOrReplaceChild(
            "connecting_parts",
            CubeListBuilder.create(),
            PartPose.offset(11.0F, 24.0F, -1.0F)
        );

        connectingParts.addOrReplaceChild(
            "f_right_r1",
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(18.0F, -5.0F, -1.0F, 2.0F, 12.0F, 2.0F, CubeDeformation.NONE),
            PartPose.offsetAndRotation(-9.0F, -23.0F, -12.0F, -0.2896F, -0.3078F, 1.9699F)
        );

        connectingParts.addOrReplaceChild(
            "f_right_r2",
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(18.0F, -5.0F, -1.0F, 2.0F, 12.0F, 2.0F, CubeDeformation.NONE),
            PartPose.offsetAndRotation(-10.0F, -23.0F, 2.0F, 0.4363F, -0.3316F, 1.8815F)
        );

        connectingParts.addOrReplaceChild(
            "f_left_r1",
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 13.0F, 2.0F, CubeDeformation.NONE),
            PartPose.offsetAndRotation(-2.0F, -8.0F, 10.0F, -0.3142F, -0.0829F, 1.117F)
        );

        connectingParts.addOrReplaceChild(
            "b_left_r1",
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 13.0F, 2.0F, CubeDeformation.NONE),
            PartPose.offsetAndRotation(-2.0F, -8.0F, -8.0F, 0.384F, -0.0829F, 1.117F)
        );

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void setupAnim(DroneEntityRenderState renderState) {
        // float spinSpeed = renderState.isControlled ? 25.0F : 4.0F;
        // float angle = renderState.ageInTicks * spinSpeed;
        // frontLeft.yRot = angle;
        // backLeft.yRot = angle;
        // backRight.yRot = angle;
        // frontRight.yRot = angle;
    }

}