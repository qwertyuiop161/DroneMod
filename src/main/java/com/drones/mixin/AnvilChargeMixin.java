package com.drones.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.drones.item.BatteryItem;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Mixin(AnvilMenu.class)
public class AnvilChargeMixin {
    
    @Shadow
    private DataSlot cost;

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true) 
    private void handleBatteryCharge(CallbackInfo ci) {
        AnvilMenu menu = (AnvilMenu)(Object)this;
        ItemStack left = menu.getSlot(AnvilMenu.INPUT_SLOT).getItem();
        ItemStack right = menu.getSlot(AnvilMenu.INPUT_SLOT).getItem();
        if (!(left.getItem() instanceof BatteryItem)) return;
        if (!right.is(Items.REDSTONE)) return;
        int currentDamage = left.getDamageValue();
        int maxDamage = left.getMaxDamage();
        int redstoneCount = right.getCount();

        int healAmount = (maxDamage/2) *Math.min(redstoneCount, 2);
        int newDamage = Math.max(0, currentDamage - healAmount);
        if (newDamage>=currentDamage) return;
        ItemStack result = left.copy();
        result.setDamageValue(newDamage);
        menu.getSlot(AnvilMenu.RESULT_SLOT).set(result);
        this.cost.set(1);
        ci.cancel();
    }
}
