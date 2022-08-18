package io.github.offsetmonkey538.villagertaming.screen.tamedvillager;

import io.github.offsetmonkey538.villagertaming.entity.IVillagerData;
import io.github.offsetmonkey538.villagertaming.screen.ModScreenHandlers;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

import static io.github.offsetmonkey538.villagertaming.screen.tamedvillager.Buttons.TEST_BUTTON_2_ID;
import static io.github.offsetmonkey538.villagertaming.screen.tamedvillager.Buttons.TEST_BUTTON_ID;

public class TamedVillagerScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final IVillagerData villagerData;

    public TamedVillagerScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, (inventory.player.world.getEntityById(buf.readVarInt()) instanceof VillagerEntity villager ? villager : null));
    }

    public TamedVillagerScreenHandler(int syncId, PlayerInventory inventory, VillagerEntity villager) {
        super(ModScreenHandlers.TAMED_VILLAGER, syncId);
        this.inventory = inventory;
        this.villagerData = (IVillagerData) villager;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        switch (id) {
            case TEST_BUTTON_ID -> this.villagerData.setStanding(false);
            case TEST_BUTTON_2_ID -> this.villagerData.setStanding(true);
            default -> throw new UnsupportedOperationException(String.format("Player [%s] pressed button with an unknown ID [%s]", player.getName().getString(), id));
        }
        return true;
    }
}
