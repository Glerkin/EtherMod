package net.glerkin.testmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.glerkin.testmod.TestMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CustomFurnaceScreen extends AbstractContainerScreen<CustomFurnaceMenu> {
    // Путь к вашей текстуре GUI печи
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TestMod.MOD_ID, "textures/gui/container/modfurnace.png");

    public CustomFurnaceScreen(CustomFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (imageWidth - font.width(title)) / 2; // Центрируем название
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Если горит уголь — рисуем пламя (на стандартной текстуре печи огонек находится по координатам 176, 12)
        if (menu.isLit()) {
            guiGraphics.blit(TEXTURE, x + 56, y + 36, 176, 12, 14, 14);
        }

        // Рисуем стрелочку прогресса
        int k = menu.getScaledProgress();
        guiGraphics.blit(TEXTURE, x + 79, y + 34, 176, 14, k + 1, 16);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
