package com.drones;


import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes {
    public static final EntityType<DroneEntity> DRONE = register(
        "drone",
        EntityType.Builder.<DroneEntity>of(DroneEntity::new, MobCategory.MISC)
            .sized(2.0f, 0.7f)
    );
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }
    public static void registerModEntityTypes() {
        CraftableDrones.LOGGER.info("Registering EntityTypes for "+CraftableDrones.MOD_ID);
    }
    public static void regiserAttributes() {
        FabricDefaultAttributeRegistry.register(DRONE, DroneEntity.createCubeAttributes());
    }
}
