package com.artur114.srptowerdefense.client.gui;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.vec.Vec2IM;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.srptowerdefense.common.network.server.SPacketAreaProtector;
import com.artur114.srptowerdefense.common.tileentity.TileEntityAreaProtector;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class GuiAreaProtector extends GuiScreen {
    public static final int radius = 10;
    public static final int size = (radius * 2) + 1;
    public final ChunkImage[] chunks = new ChunkImage[size * size];
    public boolean[] prot = new boolean[size * size];
    public GuiButton activateButton = null;
    public boolean isMapCompiled = false;
    public TileEntityAreaProtector tile;
    public int protChunks = 0;
    public int compileIndex = 0;
    public final int xSize = 272;
    public final int ySize = 192;
    public boolean online = false;
    public final BlockPos pos;
    public float prevViewRadius = 0;
    public float viewRadius = 0;
    public int curenRadius;

    public GuiAreaProtector(BlockPos pos, long[] prot, boolean active, int range) {
        this.curenRadius = range;
        this.online = active;
        this.pos = pos;

        ChunkPos center = new ChunkPos(this.pos);
        for (long chunk : prot) {
            this.setProtected(((int) (chunk)) - center.x + (size / 2), ((int) (chunk >> 32)) - center.z + (size / 2), true);
        }
    }

    @Override
    public void updateScreen() {
        if (this.online) {
            this.isMapCompiled = this.compileMap();
        }
        if (this.isMapCompiled) {
            this.prevViewRadius = this.viewRadius;
            this.viewRadius = MathHelper.clamp(this.viewRadius + 0.35F, 0, this.curenRadius);
        }
    }

    @Override
    public void initGui() {
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.addButton(new ButtonAdv(0, x + 213, y + 64, 15, 10, SRPTDMain.loc("textures/gui/pm_buttons.png"), 30, 20, 1));
        this.addButton(new ButtonAdv(1, x + 244, y + 64, 15, 10, SRPTDMain.loc("textures/gui/pm_buttons.png"), 30, 20, 0));
        this.addButton(this.activateButton = new ButtonAdv(2, x + 227, y + 151, 19, 17, SRPTDMain.loc("textures/gui/red_button.png"), 19, 51, 0));
        this.setProtected(radius, radius, true);
        this.activateButton.enabled = !this.online;


        TileEntity tileRaw = this.mc.world.getTileEntity(this.pos);

        if (tileRaw instanceof TileEntityAreaProtector) {
            this.tile = (TileEntityAreaProtector) tileRaw;
        } else {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(SRPTDMain.loc("textures/gui/gui_area_protector.png"));
        drawModalRectWithCustomSizedTexture(x, y, 0, 0, 272, 272, 272, 272);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.isMapCompiled) {
            this.drawMap(x, y, mouseX, mouseY, partialTicks);
        } else if (this.online) {
            this.drawLoading(x, y);
        }

        this.drawText(this.online ? "online" : "offline", x + 236, y + 14, this.online ? 0x5ee649 : 0x8f2823);
        this.drawText("chunks: " + (this.online ? this.protChunks : "-"), x + 236, y + 32);
        this.drawText("range: " + (this.online ? this.curenRadius : "-"), x + 236, y + 50);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.renderEngine.bindTexture(SRPTDMain.loc("textures/gui/gui_area_protector_overlay.png"));
        this.drawTexturedModalRect(x + 8, y + 8, 0, 0, 256, 256);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        for (int i = 0; i != size * size; i++) {
            if (this.chunks[i] != null) {
                this.chunks[i].dispose();
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                if (this.online) {
                    int prev = this.curenRadius;
                    this.curenRadius = Math.min(this.curenRadius + 1, radius);
                    if (prev != this.curenRadius) {
                        SPacketAreaProtector.sendRadiusChange(this.tile, this.curenRadius);
                    }
                }
                break;
            case 1:
                if (this.online) {
                    int prev = this.curenRadius;
                    this.curenRadius = Math.max(this.curenRadius - 1, 2);
                    if (prev != this.curenRadius) {
                        SPacketAreaProtector.sendRadiusChange(this.tile, this.curenRadius);
                    }
                }
                break;
            case 2:
                if (!this.online) {
                    SPacketAreaProtector.sendActivateRequest(this.tile);
                }
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.processClick(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.processClick(mouseX, mouseY, clickedMouseButton);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void messageFromServer(NBTTagCompound nbt) {
        switch (nbt.getInteger("action")) {
            case 0:
                this.activateButton.enabled = false;
                this.online = true;
                break;
            case 1:
                ChunkPos pos = BananaMC.chunkPosFromLong(nbt.getLong("pos"));
                ChunkPos center = new ChunkPos(this.pos);
                this.setProtected(pos.x - center.x + (size / 2), pos.z - center.z + (size / 2), nbt.getBoolean("state"));
                break;
        }
    }

    private void processClick(int mouseX, int mouseY, int mouseButton) {
        if (!this.online) {
            return;
        }

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        if (!(mouseX >= x + 8 && mouseX <= x + 8 + 176 && mouseY >= y + 8 && mouseY <= y + 8 + 176)) {
            return;
        }

        int curenSize = (this.curenRadius * 2) + 1;
        float scale = 176.0F / (curenSize * 16.0F);
        float offsetX = 96.0F - (curenSize * 8.0F) * scale;
        float offsetY = 96.0F - (curenSize * 8.0F) * scale;
        float localX = (mouseX - x - offsetX) / scale;
        float localY = (mouseY - y - offsetY) / scale;
        int chunkX = (int) (localX / 16) + radius - this.curenRadius;
        int chunkZ = (int) (localY / 16) + radius - this.curenRadius;

        if (chunkX == radius && chunkZ == radius) {
            return;
        }

        if (chunkX >= 0 && chunkX < size && chunkZ >= 0 && chunkZ < size) {
            ChunkPos pos = new ChunkPos(this.pos);
            SPacketAreaProtector.sendProtectRequest(this.tile, new ChunkPos(pos.x + chunkX - (size / 2), pos.z + chunkZ - (size / 2)), mouseButton == 0);
        }
    }

    private boolean compileMap() {
        if (this.isMapCompiled) {
            return true;
        }

        int count = 32;

        ChunkPos pos = new ChunkPos(this.pos);
        for (int i = 0; i != count; i++) {
            World world = this.mc.world;
            int x = this.compileIndex % size;
            int y = this.compileIndex / size;
            this.chunks[this.compileIndex] = new ChunkImage(world, new ChunkPos(pos.x + x - (size / 2), pos.z + y - (size / 2)), this.pos.getY());
            this.compileIndex++;

            if (this.compileIndex >= size * size) {
                return true;
            }
        }

        return false;
    }

    private void drawMap(int x, int y, int mouseX, int mouseY, float partialTicks) {
        int curenSize = (this.curenRadius * 2) + 1;
        float scale = 176.0F / (curenSize * 16.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.translate((float) (192 / 2) - (((float) (curenSize * 16) / 2) * scale), ((float) 192 / 2) - (((float) (curenSize * 16) / 2) * scale), 0);
        GlStateManager.scale(scale, scale, 1);
        Vec2IM vec = Vec2IM.obtain();
        float viewR = BananaMath.lerp(this.prevViewRadius, this.viewRadius, partialTicks);

        for (int i = 0; i != size * size; i++) {
            int xC = i % size;
            int zC = i / size;

            if (Math.abs(xC - radius) > this.curenRadius || Math.abs(zC - radius) > this.curenRadius) {
                continue;
            }

            int xI = 16 * (xC - radius + this.curenRadius), yI = 16 * (zC - radius + this.curenRadius);

            this.chunks[i].bindTexture();
            drawModalRectWithCustomSizedTexture(xI, yI, 0.0F, 0.0F, 16, 16, 16, 16);

            if (this.prot[i]) {
                GlStateManager.color(0.15F, 1.0F, 1.0F, 0.35F);
                GlStateManager.disableTexture2D();
                drawModalRectWithCustomSizedTexture(xI, yI, 0, 0, 16, 16, 1, 1);

                GlStateManager.color(0.15F, 1.0F, 1.0F, 0.4F);

                if (!this.isProtected(xC, zC - 1)) {
                    drawModalRectWithCustomSizedTexture(xI, yI, 0, 0, 16, 1, 1, 1);
                }
                if (!this.isProtected(xC, zC + 1)) {
                    drawModalRectWithCustomSizedTexture(xI, yI + 15, 0, 0, 16, 1, 1, 1);
                }
                if (!this.isProtected(xC - 1, zC)) {
                    drawModalRectWithCustomSizedTexture(xI, yI, 0, 0, 1, 16, 1, 1);
                }
                if (!this.isProtected(xC + 1, zC)) {
                    drawModalRectWithCustomSizedTexture(xI + 15, yI, 0, 0, 1, 16, 1, 1);
                }

                GlStateManager.enableTexture2D();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }


            float dist = vec.set((xC - radius + this.curenRadius), (zC - radius + this.curenRadius)).distance(this.curenRadius, this.curenRadius);
            GlStateManager.color(0.0F, 0.0F, 0.0F, ((dist - viewR) * 0.25F));
            GlStateManager.disableTexture2D();
            drawModalRectWithCustomSizedTexture(xI, yI, 0, 0, 16, 16, 16, 16);
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (mouseX > (xI * scale) + x + 8 && mouseX < ((xI + 16) * scale) + x + 8 && mouseY > (yI * scale) + y + 8 && mouseY < ((yI + 16) * scale) + y + 8) {
                this.mc.renderEngine.bindTexture(SRPTDMain.loc("textures/gui/gui_area_protector_selection.png"));
                drawModalRectWithCustomSizedTexture(xI, yI, 0, 0, 16, 16, 16, 16);
            }
        }

        Vec2IM.release(vec);

        GlStateManager.popMatrix();
    }

    private void setProtected(int x, int z, boolean state) {
        if (x >= 0 && x < size && z >= 0 && z < size) {
            boolean prev = this.prot[x + z * size];
            this.prot[x + z * size] = state;

            if (prev != state) {
                this.protChunks += (state ? 1 : -1);
            }
        }
    }

    private boolean isProtected(int x, int z) {
        if (x >= 0 && x < size && z >= 0 && z < size) {
            return this.prot[x + z * size];
        }
        return false;
    }

    private void drawLoading(int x, int y) {
        long time = System.currentTimeMillis() % 2000L;
        float angle = 360f * time / 2000f;
        int centerX = x + 192 / 2;
        int centerY = y + 192 / 2;
        drawRotatingTriangles(centerX, centerY, 10.0F, angle);
    }

    private void drawRotatingTriangles(int centerX, int centerY, float radius, float angle) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(centerX, centerY, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float triangleHeight = 16.0F;
        float baseHalf = 8.0F;
        for (int i = 0; i != 3; i++) {
            float currentAngle = angle + i * (360.0F / 3);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(currentAngle, 0, 0, 1);

            buffer.begin(4, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0, -radius, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            buffer.pos(baseHalf, -radius - triangleHeight, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            buffer.pos(-baseHalf, -radius - triangleHeight, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();
        }

        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private void drawText(String text, int x, int y) {
        this.drawText(text, x, y, 0x330800);
    }

    private void drawText(String text, int x, int y, int color) {
        this.fontRenderer.drawString(text, x - (this.fontRenderer.getStringWidth(text) / 2), y, color);
    }
}
