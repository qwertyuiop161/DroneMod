package com.drones.item;

import java.util.function.Function;

import com.drones.CraftableDrones;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final Item BATTERY = registerItem("battery", properties -> new Item(properties.fireResistant()));
    public static final Item PROPELLER = registerItem("propeller", properties -> new Item(properties.fireResistant()));
    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, name), function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, name)))));
    }
    public static void registerModItems() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(BATTERY);
            output.accept(PROPELLER);
        });
    }

}
