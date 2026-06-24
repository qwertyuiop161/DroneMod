package com.drones.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.drones.FreecamClient;

import net.minecraft.client.MouseHandler;

@Mixin(MouseHandler.class)
public class FreecamMouseMixin {

    @Inject(at = @At("HEAD"), method = "turnPlayer", cancellable = true)
    private void onTurnPlayerFreecam(double timeDelta, CallbackInfo ci) {
        if (FreecamClient.isActive()) {
            ci.cancel();
        }
    }
}
