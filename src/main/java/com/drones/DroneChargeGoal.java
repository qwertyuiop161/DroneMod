package com.drones;

import com.drones.block.ChargingStationBlock;
import com.drones.block.ModBlocks;
import com.jcraft.jorbis.Block;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DroneChargeGoal extends Goal {
    
    private final DroneEntity drone;
    private BlockPos targetStation = null;
    private int chargeTimer = 0;

    public DroneChargeGoal(DroneEntity drone) {
        this.drone = drone;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !drone.isControlled()&&drone.hasBattery()&& !drone.isBatteryCharged();
    }

    @Override
    public boolean canContinueToUse() {
        if (drone.isControlled()) return false;
        if (!drone.hasBattery()) return false;
        if (targetStation == null) return false;
        Level level = drone.level();
        if (!level.getBlockState(targetStation).is(ModBlocks.CHARGING_STATION)) return false;
        return !isFullyCharged();
    }

    @Override
    public void start() {
        targetStation = findNearestChargingStation();
        chargeTimer = 0;
        if (targetStation != null) {
            drone.setNoAi(false);
            drone.setNoGravity(true);
        }
    }

    @Override
    public void stop() {
        targetStation = null;
        chargeTimer = 0;
        drone.setNoGravity(false);
        drone.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (targetStation == null) return;
        Level level = drone.level();
        Vec3 landPos = new Vec3(
            targetStation.getX() + 0.5,
            targetStation.getY() + 1.0,
            targetStation.getZ() + 0.5
        );
        double dist = drone.position().distanceTo(landPos);
        if (dist > 0.5) {
            Vec3 dir = landPos.subtract(drone.position()).normalize().scale(0.3);
            drone.setDeltaMovement(dir);
            drone.setNoGravity(true);
            drone.fallDistance = 0;
            Vec3 motion = drone.getDeltaMovement();
            if (motion.horizontalDistance() > 0.01) {
                drone.setYRot((float)(Math.toDegrees(Math.atan2(-motion.x, motion.z))));
            }
        } else {
            drone.setDeltaMovement(Vec3.ZERO);
            drone.setPos(landPos.x, landPos.y, landPos.z);
            if (ChargingStationBlock.isPowered(level, targetStation)) {
                chargeTimer++;
                if (chargeTimer >= 20) {
                    chargeTimer = 0;
                    int damage = drone.getInsertedBattery().getDamageValue();
                    if (damage > 0) {
                        drone.getInsertedBattery().setDamageValue(damage);
                    }
                }
            }
        }
    }

    private boolean isFullyCharged() {
        if (!drone.hasBattery()) return false;
        return drone.getInsertedBattery().getDamageValue() == 0;
    }

    private BlockPos findNearestChargingStation() {
        Level level = drone.level();
        BlockPos dronePos = drone.blockPosition();
        int range = 64;
        BlockPos bestPos = null;
        double bestPathLength = Double.MAX_VALUE;
        for (int x  = -range; x<=range; x++) {
            for (int y = -range; y<=range; y++) {
                for (int z = -range; z<=range; z++) {
                    BlockPos candidate = dronePos.offset(x, y, z);
                    if (candidate.distSqr(dronePos) > range * range) continue;
                    if (!level.getBlockState(candidate).is(ModBlocks.CHARGING_STATION)) continue;
                    double straightDist = drone.position().distanceTo(
                        new Vec3(candidate.getX() +0.5, candidate.getY()+1.0, candidate.getZ()+0.5)
                    );
                    if (!hasLineOfSight(candidate)) continue;
                    if (straightDist<bestPathLength) {
                        bestPathLength=straightDist;
                        bestPos=candidate;
                    }
                }
            }
        }
        return bestPos;
    }
    private boolean hasLineOfSight(BlockPos target) {
        Vec3 start = drone.position().add(0, drone.getBbHeight()/2,0);
        Vec3 end = new Vec3(target.getX() +0.5, target.getY() +1.5, target.getZ()+0.5);
        Vec3 dir = end.subtract(start);
        double length = dir.length();
        Vec3 step = dir.normalize().scale(0.5);
        Vec3 current = start;
        double traveled = 0;
        while (traveled<length) {
            current = current.add(step);
            traveled+=0.5;
            BlockPos checkPos = BlockPos.containing(current);
            if (!drone.level().getBlockState(checkPos).isAir()&& drone.level().getBlockState(checkPos).isSolidRender()) {
                return false;
            }
        }
        return true;
    }
}
