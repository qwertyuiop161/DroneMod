package com.drones.item;

import com.drones.DroneControllerClient;
import com.drones.DroneEntity;
import com.drones.ModDataComponentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class DroneControllerItem extends Item {

    public DroneControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (hand !=InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (player.level().isClientSide()) return InteractionResult.SUCCESS;
        if (!(target instanceof DroneEntity drone)) return InteractionResult.PASS;
        

        ItemStack realStack = player.getItemInHand(hand);

        UUID existing = realStack.get(ModDataComponentTypes.LINKED_DRONE_UUID);
        if (existing != null&&existing.equals(drone.getUUID())) {
            realStack.remove(ModDataComponentTypes.LINKED_DRONE_UUID);
            drone.clearLink();
            player.sendSystemMessage(Component.literal("Drone unlinked!"));
            return InteractionResult.SUCCESS;
        }
        if (existing!=null) {
            player.sendSystemMessage(Component.literal("Already Linked to a drone!"));
            return InteractionResult.FAIL;
        }
        if (drone.isLinked()) {
            player.sendSystemMessage(Component.literal("Drone already linked to another controller!"));
            return InteractionResult.FAIL;
        }

        realStack.set(ModDataComponentTypes.LINKED_DRONE_UUID, drone.getUUID());
        drone.setLinkedController(player.getUUID());
        player.sendSystemMessage(Component.literal("Drone linked!"));
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            if (player.isShiftKeyDown() && stack.get(ModDataComponentTypes.LINKED_DRONE_UUID) != null) {
                DroneControllerClient.toggleCameraMode();
            }
            return InteractionResult.CONSUME;
        }
        if (player.isShiftKeyDown()) return InteractionResult.SUCCESS;
        UUID linkedUUID = stack.get(ModDataComponentTypes.LINKED_DRONE_UUID);
        if (linkedUUID==null) {
            player.sendSystemMessage(Component.literal("No Drone linked, right click a drone"));
            return InteractionResult.FAIL;
        }
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;
        Entity found = serverLevel.getEntity(linkedUUID);
        if (found instanceof DroneEntity drone) {
            double dist = Math.round(player.distanceTo(drone)*10.0)/10.0;
            String batteryInfo;
            if (!drone.hasBattery()) {
                batteryInfo = "No battery";
            } else {
                ItemStack bat = drone.getInsertedBattery();
                int percent = (int)(((float)(bat.getMaxDamage()-bat.getDamageValue())/bat.getMaxDamage())*100);
                batteryInfo = percent+"% charge";
            }
            player.sendSystemMessage(Component.literal("Drone at: " + drone.blockPosition() + ", " + dist + " blocks away | Battery: " + batteryInfo));
        } else {
            stack.remove(ModDataComponentTypes.LINKED_DRONE_UUID);
            player.sendSystemMessage(Component.literal("Linked drone not found. Link cleared."));
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        UUID linkedUUID = stack.get(ModDataComponentTypes.LINKED_DRONE_UUID);
        if (linkedUUID==null) return;
        if (!(itemEntity.level() instanceof ServerLevel serverLevel)) return;
        Entity found = serverLevel.getEntity(linkedUUID);
        if (found instanceof DroneEntity drone) {
            drone.setControlled(false);
            drone.clearLink();
            for (Player p : serverLevel.players()) {
                if (p instanceof ServerPlayer sp && sp.getCamera() == drone) {
                    sp.setCamera(sp);
                }
            }
        }
    }
}