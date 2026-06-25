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

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        Identifier offModel = Identifier.fromNamespaceAndPath("craftable-drones", "block/charging_station");
        Identifier onModel = Identifier.fromNamespaceAndPath("craftable-drones", "block/charging_station_on");
        blockModelGenerators.modelOutput.accept(offModel, () -> {
            com.google.gson.JsonObject json = new com.google.gson.JsonObject();
            json.addProperty("parent", "minecraft:block/orientable");
            com.google.gson.JsonObject textures = new com.google.gson.JsonObject();
            textures.addProperty("top", "craftable-drones:block/top_off");
            textures.addProperty("bottom", "craftable-drones:block/bottom");
            textures.addProperty("front", "craftable-drones:block/front");
            textures.addProperty("side", "craftable-drones:block/side");
            json.add("textures", textures);
            return json;
        });

        blockModelGenerators.modelOutput.accept(onModel, () -> {
            com.google.gson.JsonObject json = new com.google.gson.JsonObject();
            json.addProperty("parent", "minecraft:block/orientable");
            com.google.gson.JsonObject textures = new com.google.gson.JsonObject();
            textures.addProperty("top", "craftable-drones:block/top_on");
            textures.addProperty("bottom", "craftable-drones:block/bottom");
            textures.addProperty("front", "craftable-drones:block/front");
            textures.addProperty("side", "craftable-drones:block/side");
            json.add("textures", textures);
            return json;
        });

        MultiVariantGenerator generator = MultiVariantGenerator.dispatch(ModBlocks.CHARGING_STATION)
                .with(PropertyDispatch.initial(ChargingStationBlock.POWERED)
                        .select(false, BlockModelGenerators.plainVariant(offModel))
                        .select(true, BlockModelGenerators.plainVariant(onModel)))
                .with(PropertyDispatch.modify(ChargingStationBlock.FACING)
                        .select(Direction.NORTH, BlockModelGenerators.NOP)
                        .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
                        .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
                        .select(Direction.WEST, BlockModelGenerators.Y_ROT_270));

        blockModelGenerators.blockStateOutput.accept(generator);
        blockModelGenerators.registerSimpleItemModel(ModBlocks.CHARGING_STATION, offModel);
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
