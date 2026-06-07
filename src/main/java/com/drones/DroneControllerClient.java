package com.drones;

import java.util.UUID;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class DroneControllerClient implements ClientModInitializer {
    private static boolean inControlMode = false;
    private static boolean inCameraMode = false;
    private static boolean relativeMode = true;
    private static boolean cameraModeA = true;
    private static boolean homeWasPressed = false;
    private static boolean endWasPressed = false;
    private static boolean rWasPressed = false;
    private static boolean gWasPressed = false;
    private static boolean bWasPressed = false;
    private static float lockedYaw = 0f;
    private static float droneCameraYaw = 0f;
    private static float droneCameraPitch = 0f;
    private static float lastPlayerYaw = 0f;
    private static float lastPlayerPitch = 0f;
    private static float lockedCameraYaw = 0f;
    private static float lockedCameraPitch = 0f;
    private static double mouseDeltaX = 0;
    private static double mouseDeltaY = 0;
    private static double lastMouseX=-1;
    private static double lastMouseY=-1;
    private static DroneEntity activeDrone = null;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null)
                return;
            if (client.screen != null)
                return;
            LocalPlayer player = client.player;
            float currentPlayerYaw = player.getYRot();
            float currentPlayerPitch = player.getXRot();
            if (!inControlMode) {
                boolean hasLinked = false;
                int size = player.getInventory().getContainerSize();
                for (int i = 0; i < size; i++) {
                    ItemStack s = player.getInventory().getItem(i);
                    if (s.get(ModDataComponentTypes.LINKED_DRONE_UUID) != null) {
                        hasLinked = true;
                        break;

                    }
                }
                if (!hasLinked) {
                    if (inCameraMode)
                        exitCameraMode();
                    return;

                }
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
            rWasPressed = rNowPressed;
            boolean gNowPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_G) == GLFW.GLFW_PRESS;
            if (gNowPressed && !gWasPressed && inCameraMode) {
                cameraModeA = !cameraModeA;
                if (cameraModeA) {
                    if (activeDrone != null) {
                        lockedCameraYaw = activeDrone.getYRot();
                        lockedCameraPitch = activeDrone.getXRot();

                    }
                    player.sendSystemMessage(Component.literal("[Drone] Camera Mode A - free head, locked drone"));
                } else {
                    lockedCameraYaw = player.getYRot();
                    lockedCameraPitch = player.getXRot();
                    player.sendSystemMessage(Component.literal("camera mode b, locked head, free drone"));

                }
            }
            gWasPressed = gNowPressed;
            if (!inControlMode && !inCameraMode)
                return;
            
            boolean homePressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_HOME) == GLFW.GLFW_PRESS;
            if (homePressed && !homeWasPressed) {
                relativeMode = !relativeMode;
                if (relativeMode) {
                    player.sendSystemMessage(Component.literal("[Drone] Mode: Relative to player facing"));

                } else {
                    player.sendSystemMessage(
                            Component.literal("[Drone] Mode: Fixed direction (press END to lock direction)"));

                }
            }
            homeWasPressed = homePressed;
            boolean endPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_END) == GLFW.GLFW_PRESS;
            if (!relativeMode && endPressed && !endWasPressed) {
                lockedYaw = player.getYRot();
                player.sendSystemMessage(Component.literal("[Drone] Direction locked"));

            }
            endWasPressed = endPressed;
            float dx = 0, dy = 0, dz = 0;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS)
                dz = 1;
            else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS)
                dz = -1;
            else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS)
                dx = 1;
            else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS)
                dx = -1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_PAGE_UP) == GLFW.GLFW_PRESS)
                dy = 1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_PAGE_DOWN) == GLFW.GLFW_PRESS)
                dy = -1;
            float sendYaw, sendPitch;
            if (inCameraMode && cameraModeA) {
                sendYaw = lockedCameraYaw;
                sendPitch = lockedCameraPitch;

            } else if (inCameraMode && !cameraModeA) {
                double[] mouseX = new double[1];
                double[] mouseY = new double[1];
                GLFW.glfwGetCursorPos(window, mouseX, mouseY);
                if (lastMouseX >= 0) {
                    float sensitivity = ((Double) Minecraft.getInstance().options.sensitivity().get()).floatValue() * 0.6f + 0.2f;
                    float deltaX = (float) (mouseX[0] - lastMouseX) * sensitivity * 0.5f;
                    float deltaY = (float) (mouseY[0] - lastMouseY) * sensitivity * 0.5f;
                    droneCameraYaw += deltaX;
                    droneCameraPitch = Math.clamp(droneCameraPitch + deltaY, -90f, 90f);

                }
                lastMouseX = mouseX[0];
                lastMouseY = mouseY[0];
                sendYaw = droneCameraYaw;
                sendPitch = droneCameraPitch;
            } else {
                sendYaw = player.getYRot();
                sendPitch = player.getXRot();

            }
            if (inControlMode) {
                float movementYaw = (inCameraMode && !cameraModeA)
                    ? (float) Math.toRadians(droneCameraYaw)
                    : (float) Math.toRadians(relativeMode ? player.getYRot() : lockedYaw);
                float rdx = (float) (dx * Math.cos(movementYaw) - dz * Math.sin(movementYaw));
                float rdz = (float) (dx * Math.sin(movementYaw) + dz * Math.cos(movementYaw));
                ClientPlayNetworking.send(new DroneControlPacket(rdx, dy, rdz, sendYaw, sendPitch, false));

            }
        });
    }

    public static void enterControlMode() {
        inControlMode = true;
        relativeMode = true;
        Minecraft.getInstance().player.sendSystemMessage(
                Component.literal(
                        "[Drone] Control mode ON — arrows to move, PgUp/PgDn to ascend/descend, HOME to toggle direction mode, R to exit"));

    }

    public static void exitControlMode(Minecraft client) {
        inControlMode = false;
        exitCameraMode();
        ClientPlayNetworking.send(new DroneControlPacket(0, 0, 0, 0, 0, true));
        if (client.player != null) {
            client.player.sendSystemMessage(Component.literal("[Drone] Control mode OFF"));

        }
    }

    public static boolean isInControlMod() {
        return inControlMode;

    }

    public static void toggleCameraMode() {
        inCameraMode = !inCameraMode;
        Minecraft mc = Minecraft.getInstance();
        if (inCameraMode) {
            UUID droneUUID = mc.player.getMainHandItem().get(ModDataComponentTypes.LINKED_DRONE_UUID);
            if (droneUUID == null) {
                inCameraMode = false;
                return;

            }
            for (Entity e : mc.level.entitiesForRendering()) {
                if (e instanceof DroneEntity drone && drone.getUUID().equals(droneUUID)) {
                    activeDrone = drone;
                    lockedCameraYaw = drone.getYRot();
                    lockedCameraPitch = drone.getXRot();
                    cameraModeA = true;
                    mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
                    mc.player.sendSystemMessage(Component
                            .literal("[Drone] Camera ON (Mode A) - F to switch modes, Shift+right-click to exit"));
                    return;

                }
            }
            inCameraMode = false;
        } else {
            mc.setCameraEntity(mc.player);
            mc.player.sendSystemMessage(Component.literal("[Drone] Camera OFF"));

        }
    }

    public static void exitCameraMode() {
        if (inCameraMode) {
            inCameraMode = false;
            activeDrone = null;
            Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);

        }
    }

    public static boolean isInCameraMode() {
        return inCameraMode;

    }

    public static DroneEntity getActiveDrone() {
        return activeDrone;

    }

    public static boolean isCameraModeA() {
        return cameraModeA;

    }

    public static float getLockedCameraYaw() {
        return lockedCameraYaw;

    }

    public static float getLockedCameraPitch() {
        return lockedCameraPitch;

    }

    public static float getDroneCameraYaw() {
        return droneCameraYaw;

    }

    public static float getDroneCameraPitch() {
        return droneCameraPitch;

    }

    public static void accumulateMouseDelta(double dx, double dy) {
        mouseDeltaX += dx;
        mouseDeltaY += dy;
    }

    public static double consumeMouseDeltaX() {
        double v = mouseDeltaX;
        mouseDeltaX = 0;
        return v;
    }

    public static double consumeMouseDeltaY() {
        double v = mouseDeltaY;
        mouseDeltaY = 0;
        return v;
    }
}
