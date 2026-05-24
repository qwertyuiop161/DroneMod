package com.drones;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DroneControllerClient implements ClientModInitializer {
    private static boolean inControlMode = false;
    private static boolean relativeMode=true;
    private static boolean homeWasPressed = false; 
    private static float lockedYaw = 0f;
    private static boolean rWasPressed = false;
    private static boolean endWasPressed = false;
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) return;
            if (client.screen!=null) return;
            LocalPlayer player = client.player;
            if (!inControlMode) {
                boolean hasLinked= false;
                int size = player.getInventory().getContainerSize();
                for (int i = 0;i<size; i++) {
                    ItemStack s = player.getInventory().getItem(i);
                    if (s.get(ModDataComponentTypes.LINKED_DRONE_UUID)!=null) {
                        hasLinked=true;
                        break;
                    }
                }
                if (!hasLinked) return;
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
            rWasPressed=rNowPressed;
            if (!inControlMode) return;
            boolean homePressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_HOME) == GLFW.GLFW_PRESS;
            if (homePressed&&!homeWasPressed) {
                relativeMode=!relativeMode;
                if (relativeMode) {
                    player.sendSystemMessage(Component.literal("[Drone] Mode: Relative to player facing"));
                } else {
                    player.sendSystemMessage(Component.literal("[Drone] Mode: Fixed direction (press END to lock direction)"));
                }
            }
            homeWasPressed=homePressed;
            boolean endPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_END) == GLFW.GLFW_PRESS;
            if (!relativeMode &&endPressed&&!endWasPressed) {
                lockedYaw = player.getYRot();
                player.sendSystemMessage(Component.literal("[Drone] Direction locked"));
            }
            endWasPressed=endPressed;
            float dx=0,dy=0,dz=0;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP)==GLFW.GLFW_PRESS)dz=1;
            else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN)==GLFW.GLFW_PRESS)dz=-1;
            else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT)==GLFW.GLFW_PRESS)dx=1;
            else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT)==GLFW.GLFW_PRESS)dx=-1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_PAGE_UP)==GLFW.GLFW_PRESS)dy=1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_PAGE_DOWN)==GLFW.GLFW_PRESS)dy=-1;
            if (dx!=0||dy!=0||dz!=0) {
                float yaw = (float) Math.toRadians(relativeMode?player.getYRot():lockedYaw);
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
        Minecraft.getInstance().player.sendSystemMessage(
            Component.literal("[Drone] Control mode ON — arrows to move, PgUp/PgDn to ascend/descend, HOME to toggle direction mode, R to exit"));
    }
    public static void exitControlMode(Minecraft client) {
        inControlMode=false;
        ClientPlayNetworking.send(new DroneControlPacket(0, 0, 0, 0,0,true));
        if (client.player!=null) {
            client.player.sendSystemMessage(Component.literal("[Drone] Control mode OFF"));
        }
    }
    public static boolean isInControlMod() {
        return inControlMode;
    }
    
}
