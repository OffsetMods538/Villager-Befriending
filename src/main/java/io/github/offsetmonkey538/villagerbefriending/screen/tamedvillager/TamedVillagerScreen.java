package io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static io.github.offsetmonkey538.villagerbefriending.entrypoint.VillagerBefriendingMain.*;
import static io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager.Buttons.*;

public class TamedVillagerScreen extends HandledScreen<TamedVillagerScreenHandler> {
    private static final Identifier TEXTURE_PATH = new Identifier(MOD_ID, "textures/gui/tamed_villager.png");

    public TamedVillagerScreen(TamedVillagerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 256;
        this.backgroundHeight = 72;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE_PATH, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;

        int middleY = y + (backgroundHeight / 2);
        int buttonWidth = 80;
        int buttonHeight = 20;

        // Stay here
        addDrawableChild(ButtonWidget
                .builder(
                        Text.translatable("entity.villagerbefriending.villager.command_menu.button.stay"), button -> sendButtonPressedPacket(STAND)
                )
                .dimensions(
                        x + 6,
                        middleY - buttonHeight / 2,
                        buttonWidth, buttonHeight
                )
                .build()
        );

        // Follow me
        addDrawableChild(ButtonWidget
                .builder(
                        Text.translatable("entity.villagerbefriending.villager.command_menu.button.follow"), button -> sendButtonPressedPacket(FOLLOW)
                )
                .dimensions(
                        x + backgroundWidth / 2 - buttonWidth / 2,
                        middleY - buttonHeight / 2,
                        buttonWidth, buttonHeight
                )
                .build()
        );

        // Wander around
        addDrawableChild(ButtonWidget
                .builder(
                        Text.translatable("entity.villagerbefriending.villager.command_menu.button.wander"), button -> sendButtonPressedPacket(WANDER)
                )
                .dimensions(
                        x + backgroundWidth - 6 - buttonWidth,
                        middleY - buttonHeight / 2,
                        buttonWidth, buttonHeight
                )
                .build()
        );
    }

    private void sendButtonPressedPacket(int buttonId) {
        if (client == null || client.interactionManager == null) return;
        client.interactionManager.clickButton(getScreenHandler().syncId, buttonId);
    }
}
