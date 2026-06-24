package com.drones;

import org.lwjgl.glfw.GLFW;

import com.drones.item.ModItems;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class FreecamClient {
    private static boolean active = false;
    private static Vec3 camPos = Vec3.ZERO;
    private static float camYaw = 0f;
    private static float camPitch = 0f;
    private static double lastMouseX = -1;
    private static double lastMouseY = -1;
    private static final double SPEED = 0.5;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!active)
                return;
            LocalPlayer player = client.player;
            if (player == null || client.level == null) {
                exitFreecam();
                return;
            }
            if (client.screen != null)
                return;

            if (!player.getMainHandItem().is(ModItems.CAMERA)) {
                exitFreecam();
                return;
            }

            player.setDeltaMovement(Vec3.ZERO);
            player.fallDistance = 0;

            long window = Minecraft.getInstance().getWindow().handle();
            double[] mouseX = new double[1];
            double[] mouseY = new double[1];
            GLFW.glfwGetCursorPos(window, mouseX, mouseY);
            if (lastMouseX >= 0) {
                float sensitivity = ((Double) Minecraft.getInstance().options.sensitivity().get()).floatValue() * 0.6f
                        + 0.2f;
                float deltaX = (float) (mouseX[0] - lastMouseX) * sensitivity * 0.5f;
                float deltaY = (float) (mouseY[0] - lastMouseY) * sensitivity * 0.5f;
                camYaw += deltaX;
                camPitch = Math.clamp(camPitch + deltaY, -90f, 90f);
            }
            lastMouseX = mouseX[0];
            lastMouseY = mouseY[0];

            double moveX = 0, moveZ = 0, moveY = 0;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
                moveZ += 1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
                moveZ -= 1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
                moveX -= 1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
                moveX += 1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS)
                moveY += 1;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS)
                moveY -= 1;

            if (moveX != 0 || moveZ != 0 || moveY != 0) {
                double yawRad = Math.toRadians(camYaw);
                double forwardX = -Math.sin(yawRad);
                double forwardZ = Math.cos(yawRad);
                double rightX = Math.cos(yawRad);
                double rightZ = Math.sin(yawRad);

                double dx = (forwardX * moveZ + rightX * moveX);
                double dz = (forwardZ * moveZ + rightZ * moveX);
                double dy = moveY;

                Vec3 delta = new Vec3(dx, dy, dz);
                if (delta.lengthSqr() > 0) {
                    delta = delta.normalize().scale(SPEED);
                }
                camPos = camPos.add(delta);
            }
        });
    }

    public static void toggleFreecam() {
        if (active) {
            exitFreecam();
        } else {
            enterFreecam();
        }
    }

    private static void enterFreecam() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null)
            return;
        active = true;
        camPos = player.getEyePosition();
        camYaw = player.getYRot();
        camPitch = player.getXRot();
        lastMouseX = -1;
        lastMouseY = -1;

        mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        player.sendSystemMessage(Component.literal(
                "[Camera] Freecam ON — WASD + mouse to fly, space/shift for up/down, shift+right-click to exit"));
    }

    private static void exitFreecam() {
        if (!active)
            return;
        active = false;
        Minecraft mc = Minecraft.getInstance();
        mc.options.setCameraType(CameraType.FIRST_PERSON);
        if (mc.player != null) {
            mc.player.sendSystemMessage(Component.literal("[Camera] Freecam OFF"));
        }
    }

    public static boolean isActive() {
        return active;
    }

    public static Vec3 getCamPos() {
        return camPos;
    }

    public static float getCamYaw() {
        return camYaw;
    }

    public static float getCamPitch() {
        return camPitch;
    }
}