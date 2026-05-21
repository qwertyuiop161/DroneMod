package com.drones;

import java.util.UUID;

import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModDataComponentTypes {
    public static final DataComponentType<UUID> LINKED_DRONE_UUID = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, "linked_drone_uuid"), DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build());
    public static void register() {
        CraftableDrones.LOGGER.info("Registering Data Components for " + CraftableDrones.MOD_ID);
    }
}
