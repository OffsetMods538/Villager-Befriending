package io.github.offsetmonkey538.villagertaming.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class TamedVillagerScreenHandler extends ScreenHandler {

    public TamedVillagerScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.TAMED_VILLAGER, syncId);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }
}
