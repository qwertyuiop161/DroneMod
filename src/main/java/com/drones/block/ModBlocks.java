// package com.drones.block;

// import com.drones.CraftableDrones;
// import net.minecraft.core.Registry;
// import net.minecraft.core.registries.BuiltInRegistries;
// import net.minecraft.core.registries.Registries;
// import net.minecraft.resources.Identifier;
// import net.minecraft.resources.ResourceKey;
// import net.minecraft.world.item.BlockItem;
// import net.minecraft.world.item.Item;
// import net.minecraft.world.level.block.*;
// import net.minecraft.world.level.block.state.BlockBehaviour;

// import java.util.function.Function;

// public class ModBlocks {
//     private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
//         ResourceKey<Block> blockKey=keyOfBlock(name);
//         Block block = blockFactory.apply(settings.setId(blockKey));
//         if (shouldRegisterItem) {
//             ResourceKey<Item> itemKey = keyOfItem(name);
//             BlockItem blockItem= new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
//             Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
//         }
//         return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
//     }
//     private static ResourceKey<Block> keyOfBlock(String name) {
//         return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, name));
//     }
//     private static ResourceKey<Item> keyOfItem(String name) {
//         return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CraftableDrones.MOD_ID, name));
//     }
// }
