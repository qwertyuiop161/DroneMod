# Craftable Drones — Player Guide

So you've got a drone mod now. Here's everything you need to actually use it.

## Crafting

| Item | Recipe |
|---|---|
| Battery | Copper + Redstone + Iron |
| Propeller | Iron + Chain |
| Camera | Observer + Iron + Redstone + Glass + Spyglass |
| Drone | Propeller, Battery, Redstone, Camera, Iron (shaped) |
| Controller | Iron, Redstone, Copper, Black Dye (shaped) |
| Charging Station | Redstone, Iron, Copper, Battery, Redstone Block (shaped) |

## Getting Started

1. Craft a **Drone** and place it in the world.
2. Craft a **Controller**.
3. Right-click the drone with the controller to link them. You'll get a message and the drone starts glowing to show it's linked.
4. Right-click the same drone again with the same controller to unlink it.

Each controller only links to one drone at a time, and vice versa. Try to link something that's already taken and you'll just get a "no" message instead.

## Battery

Drones need a battery in them to fly under your control.

- Sneak + empty hand on the drone pops the battery out (drops on the ground).
- Right-click the drone while holding a battery to slot it in.
- Hover over a battery item to see its charge percentage.
- The battery drains while you're actively flying the drone. When it dies, you lose control and get a heads-up message.
- Got a dead battery? Throw it in an anvil with redstone dust — works exactly like a normal anvil repair, costs XP like usual.

## Controlling the Drone

1. Controller in hand, drone linked and nearby — press **R** to take control.
2. Arrow keys move it around horizontally. Page Up / Page Down handle altitude, totally separate from the horizontal movement.
3. **HOME** swaps between two movement styles: relative to where you're facing, or locked to a fixed direction.
4. **END** locks in whatever direction you're currently facing (only matters in fixed mode).
5. **R** again to let go.

You can't fly the drone and use camera mode at the same time in the same way — see below for how that actually works together.

## Camera Mode (Drone POV)

1. Shift + right-click the linked drone to jump into its camera. You'll go third-person so you can still see your own character while watching through the drone.
2. **G** swaps between two styles:
   - **Mode A** — your head moves freely, the drone's camera stays locked looking one direction.
   - **Mode B** — your head locks in place, and your mouse now steers the drone's camera instead.
3. Your character isn't frozen here — you can still walk around, mine, place blocks, whatever, while watching through the drone.
4. Shift + right-click again to leave camera mode.
5. If the drone dies while you're watching through it, you get kicked back to your own view automatically.

## Handheld Camera (Freecam)

Same Camera item you used to build the drone also works as a standalone freecam — no drone needed.

1. Hold the Camera item, shift + right-click to turn it on.
2. Your body locks in place completely — can't move, can't turn — while your view splits off into a floating camera.
3. WASD flies the camera around, mouse looks, Space/Shift go up and down.
4. It's true noclip — flies straight through walls and terrain, no collision at all.
5. Shift + right-click again to snap back into your body.

A couple things worth knowing: you have to keep the camera in your main hand the whole time, switching items kicks you out of freecam automatically. And since it's the same item used for crafting, shift-right-clicking a camera you're saving for later will also trigger freecam — not a bug, just how it's wired.

## Checking on Your Drone

Right-click the air (not sneaking) while holding a controller that's linked to a drone, and you'll get its coordinates, how far away it is, and its battery percentage.

## Charging Station

- Place the block, then power it with a lever, torch, redstone block, whatever you've got — it needs to stay powered to actually charge anything.
- **One thing to watch for:** the block only checks for power when something around it actually changes. If you place the station right next to a redstone block (or anything already powered before the station went down), it won't notice — it's only listening for changes after it's placed. Toggle the power source off and back on once after placing, and it'll pick it up fine from then on.
- When a drone's battery dies, it'll automatically hunt for the nearest powered station within 64 blocks and fly over to dock.
- Charges at roughly 1% a second while it's sitting there powered.
- Pull the battery out mid-charge and it keeps whatever progress it made.
- No station in range when the battery dies? The drone just sits there dead. You'll need to manually swap in a charged battery, or get it within range of a station somehow.

One honest caveat: right now the drone flies in a pretty direct line to the station and just slides along anything it bumps into rather than properly routing around obstacles. Keep the path between your drone's usual airspace and the charging station reasonably clear.

## Drone Repair

Drone took some damage? Right-click it with an iron ingot to patch it up — 4 HP (2 hearts) per ingot. It'll tell you if it's already full.

## Hazards

- Water is bad news — a drone sitting in water for about 7 seconds straight will die.
- Whenever a drone dies, it drops its battery (if it has one), no matter what killed it.

## Quick Reference

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