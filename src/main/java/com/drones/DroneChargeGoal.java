package com.drones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.drones.block.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DroneChargeGoal extends Goal {

    private final DroneEntity drone;
    private BlockPos targetStation = null;
    private int chargeTimer = 0;

    private List<BlockPos> path = null;
    private int pathIndex = 0;
    private int recheckTimer = 0;

    private static final double SPEED = 0.25;
    private static final double ARRIVE_DIST = 0.3;
    private static final int MAX_NODES = 6000;
    private static final int SEARCH_RANGE = 64;

    private static final int[][] DIRS = {
        {0, 1, 0}, {0, -1, 0},
        {0, 0, -1}, {0, 0, 1},
        {-1, 0, 0}, {1, 0, 0}
    };

    public DroneChargeGoal(DroneEntity drone) {
        this.drone = drone;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (drone.blockPosition() == null) return false;
        if (!drone.isAlive()) return false;
        return !drone.isControlled() && drone.hasBattery() && !drone.isBatteryCharged();
    }

    @Override
    public boolean canContinueToUse() {
        if (drone.isControlled()) return false;
        if (!drone.hasBattery()) return false;
        if (targetStation == null) return false;
        if (!drone.level().getBlockState(targetStation).is(ModBlocks.CHARGING_STATION)) return false;
        return !isFullyCharged();
    }

    @Override
    public void start() {
        chargeTimer = 0;
        path = null;
        pathIndex = 0;
        recheckTimer = 0;

        targetStation = findNearestChargingStation();
        drone.setChargingActive(targetStation != null);

        if (targetStation != null) {
            drone.setNoAi(false);
            drone.setNoGravity(true);
            computePath(); 
        }
    }

    @Override
    public void stop() {
        targetStation = null;
        path = null;
        pathIndex = 0;
        chargeTimer = 0;
        drone.setChargingActive(false);
        drone.setNoGravity(false);
        drone.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void tick() {
        if (targetStation == null) return;
        Level level = drone.level();

        drone.setNoGravity(true);
        drone.fallDistance = 0;

        Vec3 landPos = new Vec3(
            targetStation.getX() + 0.5,
            targetStation.getY() + 1.0,
            targetStation.getZ() + 0.5
        );

        recheckTimer++;
        if (recheckTimer >= 40) {
            recheckTimer = 0;
            if (path != null && isPathBlocked()) {
                computePath();
            }
        }
        Vec3 waypoint;
        boolean finalApproach;
        if (path != null && pathIndex < path.size()) {
            BlockPos wp = path.get(pathIndex);
            waypoint = new Vec3(wp.getX() + 0.5, wp.getY() + 0.5, wp.getZ() + 0.5);
            finalApproach = false;
        } else {
            waypoint = landPos;
            finalApproach = true;
        }
        Vec3 diff = waypoint.subtract(drone.position());
        double dist = diff.length();
        double arriveThreshold = finalApproach ? ARRIVE_DIST : 0.6;
        if (dist > arriveThreshold) {
            Vec3 dir = diff.normalize().scale(Math.min(SPEED, dist));
            Vec3 currentPos = drone.position();
            Vec3 attemptedPos = currentPos.add(dir);
            Vec3 safePos = resolveCollision(currentPos, attemptedPos);
            drone.setDeltaMovement(Vec3.ZERO);
            drone.setPos(safePos.x, safePos.y, safePos.z);
            if (diff.horizontalDistance() > 0.01) {
                drone.setYRot((float) Math.toDegrees(Math.atan2(-diff.x, diff.z)));
            }
        } else {
            if (!finalApproach) {
                pathIndex++;
            } else {
                drone.setDeltaMovement(Vec3.ZERO);
                drone.setPos(landPos.x, landPos.y, landPos.z);
                boolean powered = level.hasNeighborSignal(targetStation);
                if (!level.isClientSide() && drone.tickCount % 20 == 0) {
                    for (var p : level.players()) {
                        p.sendSystemMessage(Component.literal("[Debug] At pad, powered=" + powered));
                    }
                }
                if (powered) {
                    chargeTimer++;
                    if (chargeTimer >= 20) {
                        chargeTimer = 0;
                        int damage = drone.getInsertedBattery().getDamageValue();
                        if (damage > 0) {
                            drone.getInsertedBattery().setDamageValue(Math.max(0, damage - 10));
                        }
                    }
                }
            }
        }
    }

    private boolean isFullyCharged() {
        if (!drone.hasBattery()) return false;
        return drone.getInsertedBattery().getDamageValue() == 0;
    }
    private void computePath() {
        BlockPos start = drone.blockPosition();
        BlockPos goal = targetStation.above();
        if (start == null || goal == null) {
            path = null;
            return;
        }
        List<BlockPos> result = aStar(start, goal);
        path = result;
        pathIndex = 0;
    }

    private boolean isPathBlocked() {
        if (path == null) return false;
        for (int i = pathIndex; i < path.size(); i++) {
            if (!fitsAt(path.get(i))) return true;
        }
        return false;
    }

    private Vec3 resolveCollision(Vec3 current, Vec3 target) {
        Level level = drone.level();
        Vec3 fullMove = target.subtract(current);
        if (level.noCollision(drone, drone.getBoundingBox().move(fullMove.subtract(current.subtract(current))))) {

        }
        double dx = target.x - current.x;
        double dy = target.y - current.y;
        double dz = target.z - current.z;

        var box = drone.getBoundingBox();

        var boxX = box.move(dx, 0, 0);
        double resultX = level.noCollision(drone, boxX) ? dx : 0;

        var boxY = box.move(0, dy, 0);
        double resultY = level.noCollision(drone, boxY) ? dy : 0;

        var boxZ = box.move(0, 0, dz);
        double resultZ = level.noCollision(drone, boxZ) ? dz : 0;

        var boxCombined = box.move(resultX, resultY, resultZ);
        if (!level.noCollision(drone, boxCombined)) {
            if (level.noCollision(drone, box.move(resultX, 0, 0))) {
                resultY = 0;
                resultZ = 0;
            } else if (level.noCollision(drone, box.move(0, resultY, 0))) {
                resultX = 0;
                resultZ = 0;
            } else if (level.noCollision(drone, box.move(0, 0, resultZ))) {
                resultX = 0;
                resultY = 0;
            } else {
                resultX = 0;
                resultY = 0;
                resultZ = 0;
            }
        }
        return new Vec3(current.x + resultX, current.y + resultY, current.z + resultZ);
    }

    private List<BlockPos> aStar(BlockPos start, BlockPos goal) {
        Map<BlockPos, BlockPos> cameFrom = new HashMap<>();
        Map<BlockPos, Double> gScore = new HashMap<>();
        Set<BlockPos> closed = new HashSet<>();

        PriorityQueue<BlockPos> open = new PriorityQueue<>(Comparator.comparingDouble(p -> gScore.getOrDefault(p, Double.MAX_VALUE)+heuristic(p, goal)));
        gScore.put(start, 0.0);
        open.add(start);
        int nodesExpanded = 0;
        while (!open.isEmpty() && nodesExpanded < MAX_NODES) {
            BlockPos current = open.poll();
            if (closed.contains(current)) continue;
            closed.add(current);
            nodesExpanded++;
            if (current.equals(goal) || current.distSqr(goal) <= 1) {
                return reconstructPath(cameFrom, current);
            }
            for (int[] d : DIRS) {
                BlockPos neighbor = current.offset(d[0], d[1], d[2]);
                if (neighbor.distSqr(start) > (long) SEARCH_RANGE * SEARCH_RANGE) continue;
                if (closed.contains(neighbor)) continue;
                if (!fitsAt(neighbor)) continue;
                double tenativeG = gScore.getOrDefault(current, Double.MAX_VALUE) + 1.0;
                if (tenativeG < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tenativeG);
                    open.add(neighbor);
                }
            }
        }
        return null;
    }
    private double heuristic(BlockPos a, BlockPos b) {
        return Math.sqrt(a.distSqr(b));
    }
    private List<BlockPos> reconstructPath(Map<BlockPos, BlockPos> cameFrom, BlockPos current) {
        List<BlockPos> result = new ArrayList<>();
        BlockPos node = current;
        while (node != null) {
            result.add(node);
            node = cameFrom.get(node);
        }
        Collections.reverse(result);
        if (!result.isEmpty()) result.remove(0);
        return result;
    }
    private boolean fitsAt(BlockPos center) {
        Level level = drone.level();
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();
        for (int dx = 0; dx <= 1; dx++) {
            for (int dz = 0; dz <= 1; dz++) {
                BlockPos check = new BlockPos(cx+dx, cy, cz+dz);
                BlockState state = level.getBlockState(check);
                if (!state.isAir() && state.isSolidRender()) {
                    return false;
                }
            }
        }
        return true;
    }
    private BlockPos findNearestChargingStation() {
        Level level = drone.level();
        BlockPos dronePosRaw = drone.blockPosition();
        if (dronePosRaw == null) return null;
        final int dx0 = dronePosRaw.getX();
        final int dy0 = dronePosRaw.getY();
        final int dz0 = dronePosRaw.getZ();
        int range = SEARCH_RANGE;
        BlockPos bestPos = null;
        double bestDist = Double.MAX_VALUE;
        Vec3 dronePosVec = drone.position();
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    long distSq = (long) x*x + (long) y*y + (long) z*z;
                    if (distSq > (long) range * range) continue;
                    BlockPos candidate = new BlockPos(dx0 + x, dy0 + y, dz0 + z);
                    if (!level.getBlockState(candidate).is(ModBlocks.CHARGING_STATION)) continue;
                    Vec3 targetVec = new Vec3(
                        candidate.getX() + 0.5,
                        candidate.getY() + 1.0,
                        candidate.getZ() + 0.5
                    );
                    double dist = dronePosVec.distanceTo(targetVec);
                    if (dist<bestDist) {
                        bestDist = dist;
                        bestPos = candidate;
                    }
                }
            }
        }
        return bestPos;
    }
}