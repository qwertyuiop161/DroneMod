package com.drones.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.drones.DroneControllerClient;
import com.drones.DroneEntity;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;

@Mixin(Camera.class)
public abstract class DroneCameraMixin {
    @Shadow
    protected abstract void setRotation(float yRot, float xRot);
    @Shadow
    protected abstract void setPosition(double x, double y, double z);
    @Inject(at = @At("TAIL"), method = "alignWithEntity")
    private void onAlignWithEntity(float partialTick, CallbackInfo ci) {
        if (!DroneControllerClient.isInCameraMode()) {
            return;
        }
        DroneEntity drone = DroneControllerClient.getActiveDrone();
        if (drone == null) {
            return;
        }
        setPosition(drone.getX(), drone.getY() + drone.getEyeHeight(), drone.getZ());
        if (DroneControllerClient.isCameraModeA()) {
            setRotation(DroneControllerClient.getLockedCameraYaw(), DroneControllerClient.getLockedCameraPitch());
        } else {
            setRotation(drone.getYRot(), drone.getXRot());
        }
    }
}