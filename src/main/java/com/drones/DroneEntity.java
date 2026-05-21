package com.drones;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class DroneEntity extends PathfinderMob {

    private static final EntityDataAccessor<String> LINKED_CONTROLLER =
        SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);

    public DroneEntity(Level world) {
        this(ModEntityTypes.DRONE, world);
    }

    public DroneEntity(EntityType<? extends DroneEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LINKED_CONTROLLER, "");
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

    public boolean isLinked() {
        return !entityData.get(LINKED_CONTROLLER).isEmpty();
    }

    public UUID getLinkedController() {
        String val = entityData.get(LINKED_CONTROLLER);
        if (val.isEmpty()) return null;
        return UUID.fromString(val);
    }

    public void setLinkedController(UUID uuid) {
        entityData.set(LINKED_CONTROLLER, uuid.toString());
    }

    public void clearLink() {
        entityData.set(LINKED_CONTROLLER, "");
    }

    public void triggerExplosion() {
        if (!level().isClientSide()) {
            level().explode(this, getX(), getY(), getZ(), 3.0f, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }
}