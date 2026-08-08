package com.artur114.srptowerdefense.client.gui;

import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiAreaProtector extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        int x = (this.width - 256) / 2;
        int y = (this.height - 256) / 2;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
