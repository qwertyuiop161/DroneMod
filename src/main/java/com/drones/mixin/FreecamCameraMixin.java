package com.drones.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.drones.FreecamClient;

import net.minecraft.client.Camera;

@Mixin(Camera.class)
public abstract class FreecamCameraMixin {

    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Shadow
    protected abstract void setPosition(double x, double y, double z);

    @Inject(at = @At("TAIL"), method = "alignWithEntity")
    private void onAlignWithEntityFreecam(float partialTick, CallbackInfo ci) {
        if (!FreecamClient.isActive()) return;

        var pos = FreecamClient.getCamPos();
        setPosition(pos.x, pos.y, pos.z);
        setRotation(FreecamClient.getCamYaw(), FreecamClient.getCamPitch());
    }
}