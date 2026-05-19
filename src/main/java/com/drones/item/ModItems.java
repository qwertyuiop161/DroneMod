package com.drones.item;

import java.util.function.Function;

import com.drones.CraftableDrones;
import com.drones.ModEntityTypes;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public class ModItems {
    public static final Item BATTERY = registerItem("battery", Item::new,new Item.Properties());
    public static final Item PROPELLER = registerItem("propeller", Item::new,new Item.Properties());
    public static final Item DRONE_ITEM = registerItem("drone_item", SpawnEggItem::new, new Item.Properties().spawnEgg(ModEntityTypes.DRONE));
    public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> function, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, name));

		T item = function.apply(settings.setId(itemKey));

		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
    }
    public static void registerModItems() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(BATTERY);
            output.accept(PROPELLER);
        });
    }

}
