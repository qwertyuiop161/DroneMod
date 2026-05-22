package com.drones;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces.MineShaftCorridor;

public class DroneControllerClient implements ClientModInitializer {
    private static boolean inControlMode = false;
    private static boolean relativeMode=true;
    private static float lockedYaw = 0f;
    private static boolean rWasPressed = false;
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) return;
            LocalPlayer player = client.player;
            ItemStack held = player.getMainHandItem();
            if (held.get(ModDataComponentTypes.LINKED_DRONE_UUID) == null) {
                if (inControlMode) exitControlMode(client);
                return;
            }
            long window = Minecraft.getInstance().getWindow().handle();
            boolean rNowPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_R) == GLFW.GLFW_PRESS;
            if (rNowPressed && !rWasPressed) {
                if (inControlMode) {
                    exitControlMode(client);
                } else {
                    enterControlMode();
                }
            }
            if (!inControlMode) return;
            boolean homePressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_HOME) == GLFW.GLFW_PRESS;
            if (homePressed) {
                relativeMode=!relativeMode;
                if (relativeMode) {
                    player.sendSystemMessage(Component.literal("[Drone] Mode: Relative to player facing"));
                } else {
                    player.sendSystemMessage(Component.literal("[Drone] Mode: Fixed direction (press END to lock direction)"));
                }
            }
            boolean endPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_END) == GLFW.GLFW_PRESS;
            if (!relativeMode &&endPressed) {
                lockedYaw = player.getYRot();
                player.sendSystemMessage(Component.literal("[Drone] Direction locked"));
            }
            float dx=0,dy=0,dz=0;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP)==GLFW.GLFW_PRESS)dz=1;
            else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN)==GLFW.GLFW_PRESS)dz=-1;
            else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT)==GLFW.GLFW_PRESS)dx=1;
            else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT)==GLFW.GLFW_PRESS)dx=-1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_PAGE_UP)==GLFW.GLFW_PRESS)dy=1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_PAGE_DOWN)==GLFW.GLFW_PRESS)dy=-1;
            if (dx!=0||dy!=0||dz!=0) {
                float yaw = (float) Math.toRadians(player.getYRot());
                float rdx = (float) (dx*Math.cos(yaw)-dz*Math.sin(yaw));
                float rdz = (float) (dx*Math.sin(yaw)+dz*Math.cos(yaw));
                ClientPlayNetworking.send(new DroneControlPacket(rdx, dy, rdz, player.getYRot(), player.getXRot(), false));
            } else {
                ClientPlayNetworking.send(new DroneControlPacket(0, 0, 0, player.getYRot(), player.getXRot(), false));
            }
        });
    }
    public static void enterControlMode() {
        inControlMode=true;
        relativeMode=true;
        rWasPressed=true;
        Minecraft.getInstance().player.sendSystemMessage(
            Component.literal("[Drone] Control mode ON — arrows to move, PgUp/PgDn to ascend/descend, HOME to toggle direction mode, R to exit"));
    }
    public static void exitControlMode(Minecraft client) {
        inControlMode=false;
        rWasPressed=true;
        ClientPlayNetworking.send(new DroneControlPacket(0, 0, 0, 0,0,true));
        if (client.player!=null) {
            client.player.sendSystemMessage(Component.literal("[Drone] Control mode OFF"));
        }
    }
    public static boolean isInControlMod() {
        return inControlMode;
    }
    
}
