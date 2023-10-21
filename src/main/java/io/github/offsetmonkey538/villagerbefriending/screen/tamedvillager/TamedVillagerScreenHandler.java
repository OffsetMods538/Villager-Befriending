package io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager;

import io.github.offsetmonkey538.villagerbefriending.entity.IVillagerData;
import io.github.offsetmonkey538.villagerbefriending.screen.ModScreenHandlers;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

import static io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager.Buttons.*;
import static io.github.offsetmonkey538.villagerbefriending.entrypoint.VillagerBefriendingMain.LOGGER;

public class TamedVillagerScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final IVillagerData villagerData;

    public TamedVillagerScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, (inventory.player.getWorld().getEntityById(buf.readVarInt()) instanceof VillagerEntity villager ? villager : null));
    }

    public TamedVillagerScreenHandler(int syncId, PlayerInventory inventory, VillagerEntity villager) {
        super(ModScreenHandlers.TAMED_VILLAGER, syncId);
        this.inventory = inventory;
        this.villagerData = (IVillagerData) villager;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        switch (id) {
            case STAND -> {
                this.villagerData.setStanding(true);
                this.villagerData.setFollowingOwner(false);
            }
            case FOLLOW -> {
                this.villagerData.setStanding(false);
                this.villagerData.setFollowingOwner(true);
            }
            case WANDER -> {
                this.villagerData.setStanding(false);
                this.villagerData.setFollowingOwner(false);
            }
            default -> {
                LOGGER.warn("Player [{}] pressed button with an unknown ID [{}]!", player.getName().getString(), id);
                return false;
            }
        }
        return true;
    }
}
