package io.github.offsetmonkey538.villagertaming.screen;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

import static io.github.offsetmonkey538.villagertaming.client.screen.TamedVillagerScreen.TEST_BUTTON_2_ID;
import static io.github.offsetmonkey538.villagertaming.client.screen.TamedVillagerScreen.TEST_BUTTON_ID;
import static io.github.offsetmonkey538.villagertaming.packet.ModPackets.TAMED_VILLAGER_MENU_BUTTON_PRESSED;
import static io.github.offsetmonkey538.villagertaming.entrypoint.VillagerTamingMain.LOGGER;

public class TamedVillagerScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    public TamedVillagerScreenHandler(int syncId, PlayerInventory inventory) {
        super(ModScreenHandlers.TAMED_VILLAGER, syncId);
        this.inventory = inventory;

        ServerPlayNetworking.registerGlobalReceiver(TAMED_VILLAGER_MENU_BUTTON_PRESSED, (server, player, handler, buf, responseSender) -> {
            int buttonId = buf.readInt();
            server.execute(() -> {
                switch (buttonId) {
                    case TEST_BUTTON_ID -> LOGGER.info("First button has been pressed by {}", player.getDisplayName());
                    case TEST_BUTTON_2_ID -> LOGGER.info("Second button has been pressed {}", player.getDisplayName());
                    default -> LOGGER.warn("Button with id {} not found!", buttonId);
                }
            });
        });
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }
}
