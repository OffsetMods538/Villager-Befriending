package io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import static io.github.offsetmonkey538.villagerbefriending.entrypoint.VillagerBefriendingMain.MOD_ID;
import static io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager.Buttons.*;

public class TamedVillagerScreen extends HandledScreen<TamedVillagerScreenHandler> {
    private static final Identifier TEXTURE_PATH = new Identifier(MOD_ID, "textures/gui/tamed_villager.png");

    public TamedVillagerScreen(TamedVillagerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 256;
        this.backgroundHeight = 72;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_PATH);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title, (float)this.titleX, (float)this.titleY, 0x404040);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;

        int middleY = y + (backgroundHeight / 2);
        int buttonWidth = 80;
        int buttonHeight = 20;

        // Stay here
        addDrawableChild(new ButtonWidget(x + 6, middleY - buttonHeight / 2, buttonWidth, buttonHeight, new TranslatableText(String.format("entity.%s.villager.command_menu.button.stay", MOD_ID)), button ->
            sendButtonPressedPacket(STAND)
        ));

        // Follow me
        addDrawableChild(new ButtonWidget(x + (backgroundWidth / 2) - buttonWidth / 2, middleY - buttonHeight / 2, buttonWidth, buttonHeight, new TranslatableText(String.format("entity.%s.villager.command_menu.button.follow", MOD_ID)), button ->
            sendButtonPressedPacket(FOLLOW)
        ));

        // Wander around
        addDrawableChild(new ButtonWidget((x + (backgroundWidth) - 6) - buttonWidth, middleY - buttonHeight / 2, buttonWidth, buttonHeight, new TranslatableText(String.format("entity.%s.villager.command_menu.button.wander", MOD_ID)), button ->
            sendButtonPressedPacket(WANDER)
        ));
    }

    private void sendButtonPressedPacket(int buttonId) {
        if (client == null || client.interactionManager == null) return;
        client.interactionManager.clickButton(getScreenHandler().syncId, buttonId);
    }
}
