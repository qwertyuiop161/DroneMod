package com.drones;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class DroneEntityRenderState extends LivingEntityRenderState {
        public boolean isControlled = false;
        public float ageInTicks = 0f;
}
