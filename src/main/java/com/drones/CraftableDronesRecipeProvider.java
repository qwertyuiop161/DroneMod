package com.drones;

import java.util.concurrent.CompletableFuture;

import com.drones.item.ModItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class CraftableDronesRecipeProvider extends FabricRecipeProvider {

    public CraftableDronesRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "CraftableDronesRecipeProvider";
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider reistries, RecipeOutput exporter) {
        return new RecipeProvider(reistries, exporter) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
                shaped(RecipeCategory.REDSTONE, ModItems.BATTERY, 1)
                    .pattern("wlw")
                    .pattern("lil")
                    .pattern("wlw")
                    .define('w', Items.COPPER_INGOT)
                    .define('l', Items.REDSTONE)
                    .define('i', Items.IRON_INGOT)
                    .group("battery")
                    .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                    .save(output);
                shaped(RecipeCategory.REDSTONE, ModItems.PROPELLER, 1)
                    .pattern("wlw")
                    .pattern("wlw")
                    .pattern(" l ")
                    .define('w', Items.IRON_INGOT)
                    .define('l', Items.IRON_CHAIN)
                    .group("propeller")
                    .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                    .save(output);
                shaped(RecipeCategory.REDSTONE, ModItems.CAMERA, 1)
                    .pattern("lwl")
                    .pattern("kgk")
                    .pattern("lnl")
                    .define('w', Items.OBSERVER)
                    .define('l', Items.IRON_INGOT)
                    .define('k', Items.REDSTONE)
                    .define('g', Items.GLASS)
                    .define('n', Items.SPYGLASS)
                    .group("camera")
                    .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                    .save(output);
            }
        };
    }
    
}
