package com.artur114.srptowerdefense.client.gui;

import com.artur114.srptowerdefense.client.util.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ButtonAdv extends GuiButton {
    protected final ResourceLocation atlas;
    protected final int atlasSizeX;
    protected final int atlasSizeY;
    protected final int atlasIndex;

    public ButtonAdv(int buttonId, int x, int y, int width, int height, ResourceLocation atlas, int atlasSizeX, int atlasSizeY, int atlasIndex) {
        super(buttonId, x, y, "");
        this.atlasSizeX = atlasSizeX;
        this.atlasSizeY = atlasSizeY;
        this.atlasIndex = atlasIndex;
        this.height = height;
        this.width = width;
        this.atlas = atlas;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            int i = this.enabled ? this.hovered ? 1 : 0 : 2;

            mc.renderEngine.bindTexture(this.atlas);
            RenderHandler.renderTextureAtlas(this.x, this.y, this.width * this.atlasIndex, this.height * i, this.atlasSizeX, this.atlasSizeY, this.width, this.height);
        }
    }
}
