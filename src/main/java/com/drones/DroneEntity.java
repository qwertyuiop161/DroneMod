package com.drones;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class DroneEntity extends PathfinderMob {

    private static final EntityDataAccessor<String> LINKED_CONTROLLER =
        SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> IS_CONTROLLED = 
        SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);

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
        builder.define(IS_CONTROLLED, false);
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
    public boolean isControlled() {
        return entityData.get(IS_CONTROLLED);
    }
    public void setControlled(boolean controlled) {
        entityData.set(IS_CONTROLLED, controlled);
        this.setNoAi(controlled);
    }
    public void clearLink() {
        entityData.set(LINKED_CONTROLLER, "");
    }
    @Override
    public void tick() {
        super.tick();
        if (isControlled()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.fallDistance=0;
            this.setNoGravity(true);
        } else {
            this.fallDistance = 0;
            if (!this.onGround()) {
                this.setDeltaMovement(this.getDeltaMovement().x*0.5, -0.1, this.getDeltaMovement().z*0.5);
            }
        }
    }
    public void triggerExplosion() {
        if (!level().isClientSide()) {
            level().explode(this, getX(), getY(), getZ(), 3.0f, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }
    @Override
    public void remove(RemovalReason reason) {
        if (!level().isClientSide()) {
            setControlled(false);
        }
        super.remove(reason);
    }
}