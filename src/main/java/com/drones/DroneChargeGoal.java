package com.drones;

import java.util.EnumSet;

import com.drones.block.ChargingStationBlock;
import com.drones.block.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DroneChargeGoal extends Goal {

    private final DroneEntity drone;
    private BlockPos targetStation = null;
    private int chargeTimer = 0;
    private static final double SPEED = 0.25;
    private static final double ARRIVE_DIST = 0.3;

    public DroneChargeGoal(DroneEntity drone) {
        this.drone = drone;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        return !drone.isControlled() && drone.hasBattery() && !drone.isBatteryCharged();
    }

    public boolean canContinueToUse() {
        if (drone.isControlled()) return false;
        if (!drone.hasBattery()) return false;
        if (targetStation == null) return false;
        if (!drone.level().getBlockState(targetStation).is(ModBlocks.CHARGING_STATION)) return false;
        return !isFullyCharged();
    }

    public void start() {
        targetStation = findNearestChargingStation();
        chargeTimer = 0;
        drone.setChargingActive(targetStation != null);
        if (targetStation != null) {
            drone.setNoAi(false);
            drone.setNoGravity(true);
        }
    }

    public void stop() {
        targetStation = null;
        chargeTimer = 0;
        drone.setChargingActive(false);
        drone.setNoGravity(false);
        drone.setDeltaMovement(Vec3.ZERO);
    }

    public void tick() {
        if (targetStation == null) return;
        Level level = drone.level();
        Vec3 landPos = new Vec3(
            targetStation.getX() + 0.5,
            targetStation.getY() + 1.0,
            targetStation.getZ() + 0.5
        );
        Vec3 diff = landPos.subtract(drone.position());
        double dist = diff.length();
        drone.setNoGravity(true);
        drone.fallDistance = 0;
        if (dist > ARRIVE_DIST) {
            Vec3 dir = diff.normalize().scale(Math.min(SPEED, dist));
            drone.setDeltaMovement(Vec3.ZERO);
            drone.setPos(
                drone.getX() + dir.x,
                drone.getY() + dir.y,
                drone.getZ() + dir.z
            );
            if (diff.horizontalDistance() > 0.01) {
                drone.setYRot((float) Math.toDegrees(Math.atan2(-diff.x, diff.z)));
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
                        drone.getInsertedBattery().setDamageValue(Math.max(0, damage-10));
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
        int r = 64;
        BlockPos bestPos = null;
        double bestDist = Double.MAX_VALUE;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos candidate = dronePos.offset(x, y, z);
                    if (candidate.distSqr(bestPos) > (double) r * r) continue;
                    if (!level.getBlockState(candidate).is(ModBlocks.CHARGING_STATION)) continue;
                    Vec3 targetVec = new Vec3(
                        candidate.getX() + 0.5,
                        candidate.getY() + 1.0,
                        candidate.getZ() + 0.5
                    );
                    double dist = drone.position().distanceTo(targetVec);
                    if (!hasLineOfSight(candidate)) continue;
                    if (dist<bestDist) {
                        bestDist = dist;
                        bestPos = candidate;
                    }
                }
            }
        }
        return bestPos;
    }
    private boolean hasLineOfSight(BlockPos target) {
        Vec3 start = drone.position().add(0, drone.getBbHeight()/2.0, 0);
        Vec3 end = new Vec3(target.getX()+0.5, target.getY()+1.5, target.getZ()+0.5);
        Vec3 dir = end.subtract(start);
        double length = dir.length();
        Vec3 step = dir.normalize().scale(0.4);
        Vec3 current = start;
        double traveled = 0;
        while (traveled < length) {
            current = current.add(step);
            traveled += 0.4;
            BlockPos checkPos = BlockPos.containing(current);
            BlockPos stationPos = target;
            if (checkPos.equals(stationPos) || checkPos.equals(stationPos.above())) continue;
            if (!drone.level().getBlockState(checkPos).isAir() && drone.level().getBlockState(checkPos).isSolidRender()) {
                return false;
            }
        }
        return true;
    }
}