package com.drones.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.drones.DroneControllerClient;

import net.minecraft.client.MouseHandler;

@Mixin(MouseHandler.class)
public class DroneMouseMixin {

    @Inject(at = @At("HEAD"), method = "turnPlayer", cancellable = true)
    private void onTurnPlayer(double timeDelta, CallbackInfo ci) {
        if (!DroneControllerClient.isInCameraMode()) return;
        if (DroneControllerClient.isCameraModeA()) return;
        ci.cancel();
    }
}