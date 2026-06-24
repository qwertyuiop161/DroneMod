package com.drones.datagen;

import com.drones.block.ChargingStationBlock;
import com.drones.block.ModBlocks;
import com.drones.item.ModItems;
import com.jcraft.jorbis.Block;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;

public class ModModelProvider extends FabricModelProvider{

    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        MultiVariantGenerator generator = MultiVariantGenerator.dispatch(ModBlocks.CHARGING_STATION)
        .with(PropertyDispatch.initial(ChargingStationBlock.POWERED)
        .select(false, 
            BlockModelGenerators.plainVariant(
                Identifier.fromNamespaceAndPath("craftable-drones", "block/charging_station")))
        .select(true, 
            BlockModelGenerators.plainVariant(
                Identifier.fromNamespaceAndPath("craftable-drones", "block/charging_station_on"))))
        .with(PropertyDispatch.modify(ChargingStationBlock.FACING)
        .select(Direction.NORTH, BlockModelGenerators.NOP)
        .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
        .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
        .select(Direction.WEST, BlockModelGenerators.Y_ROT_270));
        blockModelGenerators.blockStateOutput.accept(generator);
        blockModelGenerators.registerSimpleItemModel(ModBlocks.CHARGING_STATION, Identifier.fromNamespaceAndPath("craftable-drones", "block/charging_station"));
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModItems.BATTERY, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.PROPELLER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.DRONE_ITEM, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.DRONE_CONTROLLER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CAMERA, ModelTemplates.FLAT_ITEM);
    }
    
}
