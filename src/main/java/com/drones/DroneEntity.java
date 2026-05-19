package com.drones;

import java.rmi.registry.Registry;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;

public class DroneEntity extends PathfinderMob {

    public DroneEntity(Level world) {
        this(ModEntityTypes.DRONE, world);
    }
    public DroneEntity(EntityType<? extends DroneEntity> entityType, Level world) {
        super(entityType, world);
    }
    public static AttributeSupplier.Builder createCubeAttributes() {
        return PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20)
            .add(Attributes.MOVEMENT_SPEED, 1);
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomStrollGoal(this, 0));
    }
}
