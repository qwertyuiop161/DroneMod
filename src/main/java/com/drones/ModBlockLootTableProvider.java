package com.drones;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import com.drones.block.ModBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModBlockLootTableProvider extends SimpleFabricLootTableSubProvider {
    public ModBlockLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, LootContextParamSets.BLOCK);
    }
    


    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        ModBlocks.CHARGING_STATION.getLootTable().ifPresent(key -> output.accept(key, 
            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ModBlocks.CHARGING_STATION.asItem())))));
    }
}
