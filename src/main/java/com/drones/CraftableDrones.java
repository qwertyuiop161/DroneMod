package com.drones;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drones.item.ModItems;

public class CraftableDrones implements ModInitializer {
	public static final String MOD_ID = "craftable-drones";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
	}
}