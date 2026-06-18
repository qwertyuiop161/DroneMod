package com.drones.item;

import java.util.List;

import net.minecraft.network.chat.Component;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

public class BatteryItem extends Item {
    public BatteryItem(Properties properties) {
        super(properties);
    }
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamageValue();
        int percent = (int)(((float)(maxDamage-damage)/maxDamage)*100);
        tooltip.add(Component.literal("Charge: " + percent + "%"));
    }
    public boolean isValidRepairItem(ItemStack battery, ItemStack ingredient) {
        return ingredient.is(Items.REDSTONE);
    }
}
