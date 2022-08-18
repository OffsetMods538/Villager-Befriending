package io.github.offsetmonkey538.villagertaming.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.offsetmonkey538.villagertaming.screen.TamedVillagerScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static io.github.offsetmonkey538.villagertaming.entrypoint.VillagerTamingMain.MOD_ID;

public class TamedVillagerScreen extends HandledScreen<TamedVillagerScreenHandler> {
    private static final Identifier TEXTURE_PATH = new Identifier(MOD_ID, "textures/gui/tamed_villager.png");

    public static final int TEST_BUTTON_ID = 1;
    public static final int TEST_BUTTON_2_ID = 2;

    public TamedVillagerScreen(TamedVillagerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
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

        addDrawableChild(new ButtonWidget(width / 2, height / 2, 20, 20, Text.of("Test"), button ->
            sendButtonPressedPacket(TEST_BUTTON_ID)
        ));

        addDrawableChild(new ButtonWidget(x + (backgroundWidth / 2 - 20), y + 20, 40, 20, Text.of("Test 2"), button ->
            sendButtonPressedPacket(TEST_BUTTON_2_ID)
        ));
    }

    private void sendButtonPressedPacket(int buttonId) {
        if (client == null || client.interactionManager == null) return;
        client.interactionManager.clickButton(getScreenHandler().syncId, buttonId);
    }
}
