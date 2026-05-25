package com.drones;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

import net.minecraft.network.chat.Component;

import com.drones.item.ModItems;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;

public class DroneEntity extends PathfinderMob {

    private static final EntityDataAccessor<String> LINKED_CONTROLLER =
        SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> IS_CONTROLLED = 
        SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_BATTERY = 
        SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);
    private ItemStack insertedBattery = new ItemStack(ModItems.BATTERY);
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
        builder.define(HAS_BATTERY, true);
    }

    public static AttributeSupplier.Builder createCubeAttributes() {
        return PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20)
            .add(Attributes.MOVEMENT_SPEED, 1);
    }

    public boolean isLinked() {
        return !entityData.get(LINKED_CONTROLLER).isEmpty();
    }
    public UUID getLinkedController() {
        String val = entityData.get(LINKED_CONTROLLER);
        if (val.isEmpty()) return null;
        return UUID.fromString(val);
    }
    public boolean hasBattery() {
        return entityData.get(HAS_BATTERY);
    }
    public void setBattery(boolean has) {
        entityData.set(HAS_BATTERY, has);
    }
    public ItemStack getInsertedBattery() {
        return insertedBattery;
    }
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        ItemStack heldItem = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && heldItem.isEmpty()) {
            if (!hasBattery()) {
                player.sendSystemMessage(Component.literal("No battery to eject"));
                return InteractionResult.FAIL;
            }
            if (isControlled()) {
                setControlled(false);
            }
            ItemEntity ejected = new ItemEntity(level(), getX(), getY(), getZ(), insertedBattery.copy());
            level().addFreshEntity(ejected);
            insertedBattery=new ItemStack(ModItems.BATTERY);
            setBattery(false);
            player.sendSystemMessage(Component.literal("Battery ejected"));
            return InteractionResult.SUCCESS;
        }
        if (heldItem.is(ModItems.BATTERY)) {
            if (hasBattery()) {
                player.sendSystemMessage(Component.literal("Drone already has a battery"));
                return InteractionResult.FAIL;
            }
            insertedBattery=heldItem.copy();
            insertedBattery.setCount(1);
            heldItem.shrink(1);
            setBattery(true);
            player.sendSystemMessage(Component.literal("Battery inserted"));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    public void setLinkedController(UUID uuid) {
        entityData.set(LINKED_CONTROLLER, uuid.toString());
    }
    public boolean isControlled() {
        return entityData.get(IS_CONTROLLED);
    }
    public void setControlled(boolean controlled) {
        this.setNoGravity(controlled);
        entityData.set(IS_CONTROLLED, controlled);
        this.setNoAi(controlled);
    }
    public void clearLink() {
        entityData.set(LINKED_CONTROLLER, "");
    }
    @Override
    public void tick() {
        if (isControlled()) {
            this.setNoGravity(true);
            this.setDeltaMovement(Vec3.ZERO);
            this.fallDistance = 0;
        }
        super.tick();
        if (isControlled()) {
            this.setNoGravity(true);
            this.setDeltaMovement(Vec3.ZERO);
            this.fallDistance = 0;
        } else {
            this.setNoGravity(false);
            this.fallDistance = 0;
            if (!this.onGround()) {
                this.setDeltaMovement(this.getDeltaMovement().x*0.5, -0.1, this.getDeltaMovement().z*0.5);
            }
        }
    }
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }
    @Override
    public void remove(RemovalReason reason) {
        if (!level().isClientSide()) {
            setControlled(false);
            for (Player p : level().players()) {
                if (p instanceof ServerPlayer sp) {
                    if (sp.getCamera()==this) {
                        sp.setCamera(sp);
                        sp.sendSystemMessage(Component.literal("[Drone] Camera OFF - drone destroyed"));
                    }
                }
            }
        }
        super.remove(reason);
    }
    @Override
    public boolean save(ValueOutput output) {
        super.save(output);
        output.putBoolean("HasBattery", hasBattery());
        if (hasBattery() && !insertedBattery.isEmpty()) {
            output.store("InsertedBattery", ItemStack.CODEC, insertedBattery);
        }
        output.putString("LinkedController", entityData.get(LINKED_CONTROLLER));
        return true;
    }
    @Override
    public void load(ValueInput input) {
        super.load(input);
        setBattery(input.getBooleanOr("HasBattery", true));
        input.read("InsertedBattery", ItemStack.CODEC).ifPresent(stack -> insertedBattery=stack);
        entityData.set(LINKED_CONTROLLER, input.getStringOr("LinkedController", ""));
    }
}