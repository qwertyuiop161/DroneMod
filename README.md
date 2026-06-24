# Craftable Drones —  Guide

## Crafting

| Item | Recipe |
|---|---|
| Battery | Copper + Redstone + Iron |
| Propeller | Iron + Chain |
| Camera (drone part) | Observer + Iron + Redstone + Glass + Spyglass |
| Drone | Propeller, Battery, Redstone, Camera, Iron (shaped) |
| Controller | Iron, Redstone, Copper, Black Dye (shaped) |
| Charging Station | Redstone, Iron, Copper, Battery, Redstone Block (shaped) |
| Handheld Camera (freecam tool) | *(ask in-game for current recipe)* |

## Getting Started

1. Craft a **Drone** and place it in the world like a normal entity (spawn egg or summon, depending on your setup).
2. Craft a **Controller** item.
3. **Right-click the drone with the Controller** to link them. You'll see a confirmation message and the drone will start glowing while linked.
4. Right-click the same drone again with the same controller to **unlink**.

A controller can only be linked to one drone at a time, and a drone can only be linked to one controller. If you try to link an already-linked drone or controller, you'll get a failure message.

## Battery

- Drones need a battery to fly under player control.
- **Sneak + empty hand** on the drone → ejects the battery (drops as an item).
- **Right-click the drone holding a battery** → inserts it.
- The battery item's tooltip shows its current charge percentage.
- Battery drains while the drone is being actively controlled. When it dies, the drone loses control and notifies the linked player.
- **Repairing a battery:** Put it in an anvil with **redstone dust** — works like a normal anvil repair, restoring charge for XP cost.

## Controlling the Drone

1. With the controller in hand and a linked drone nearby, press **R** to enter control mode.
2. **Arrow keys** move the drone horizontally; **Page Up / Page Down** move it vertically (independent of horizontal movement).
3. **HOME** toggles between relative-direction and fixed-direction movement modes.
4. **END** locks in your current facing as the fixed direction (only relevant in fixed mode).
5. Press **R** again to exit control mode.

While controlling the drone, you can't also be in camera mode at the same time as moving freely — see below for how camera mode works alongside this.

## Camera Mode (Drone POV)

1. **Shift + right-click** the linked drone to enter camera mode. Your character switches to third-person so you can see yourself while watching through the drone.
2. Press **G** to toggle between two camera styles:
   - **Mode A** — your head moves freely; the drone's camera view stays locked to a fixed direction.
   - **Mode B** — your head is locked; moving your mouse steers the drone's camera view.
3. You can still walk, mine, and place blocks normally while in camera mode — it doesn't restrict your character.
4. Shift + right-click the drone again to exit camera mode.
5. If the drone is destroyed while you're viewing through it, camera mode exits automatically.

## Handheld Camera (Freecam)

A separate tool from the drone — a standalone item that lets you detach your view from your body entirely, like a free-floating camera.

1. Hold the **Camera** item and **shift + right-click** to activate freecam.
2. Your body freezes in place (it can't move or turn) and your view splits off into a free-floating camera.
3. Move the camera with **WASD**, look around with the **mouse**, and go up/down with **Space** / **Shift**.
4. The freecam **ignores all collision** — it flies straight through walls, terrain, anything.
5. Shift + right-click again to exit and snap your view back to your body.

**Notes:**
- You must keep the Camera item in your main hand the whole time — switching items automatically exits freecam.
- Your character stays exactly where it was the entire time freecam is active; nothing can move it.

## Checking Drone Status

Right-click the air while holding a controller linked to a drone (not sneaking) to get a status readout: the drone's coordinates, distance from you, and current battery percentage.

## Charging Station

- Place a **Charging Station** block and power it with redstone (lever, torch, block, etc.) — it must stay powered to charge.
- When a drone's battery dies, it will automatically search for the nearest powered charging station within 64 blocks and fly there to recharge.
- Charging rate: roughly 1% per second while docked and the station is powered.
- If you eject the battery while it's mid-charge, it keeps whatever charge it had gained.
- If no charging station is in range when the battery dies, the drone just sits where it died — bring it a freshly charged battery manually, or push/carry it within range of a station.

**Note:** the drone currently flies in a fairly direct line toward the charging station and will slide along obstacles rather than always routing cleverly around them — keep a relatively clear path between your drone's usual flying area and your charging station for best results.

## Drone Repair

- If your drone takes damage (from water, collisions, or other hazards), right-click it with an **iron ingot** to heal it.
- Each ingot restores a set amount of health; you'll get a message if it's already at full health.

## Hazards

- **Water** will damage and eventually destroy an uncontrolled or controlled drone over roughly 7 seconds of continuous submersion — keep it away from water.
- A destroyed drone drops its battery (if it had one) regardless of cause of death.

## Quick Reference: Keybinds

| Key | Action |
|---|---|
| Right-click drone | Link/unlink controller |
| Shift + empty hand on drone | Eject battery |
| Right-click drone with battery | Insert battery |
| Right-click drone with iron ingot | Repair drone |
| Right-click air (with controller) | Status readout |
| Shift + right-click drone | Toggle camera mode |
| R | Toggle control mode |
| Arrow keys | Move horizontally |
| Page Up / Page Down | Move vertically |
| HOME | Toggle relative/fixed direction |
| END | Lock current direction (fixed mode) |
| G | Toggle camera mode A/B (while in camera mode) |
| Shift + right-click (holding Camera item) | Toggle freecam |
| WASD (in freecam) | Move camera |
| Space / Shift (in freecam) | Move camera up/down |
| Mouse (in freecam) | Look around |