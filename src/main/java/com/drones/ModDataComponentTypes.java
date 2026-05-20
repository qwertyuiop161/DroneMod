package com.drones;

import net.minecraft.core.Registry;
import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;


public class ModDataComponentTypes {
    public static final DataComponentType<UUID> LINKED_ENTITY_UUID = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, "linked_entity_uuid"),
        DataComponentType.<UUID>builder()
            .persistent(UUIDUtil.CODEC)
            .build()
    );
    public static void register() {}
}
