package scripts.classes

import com.artur114.bananalib.mc.BananaMC
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World
import net.minecraft.world.biome.Biome;

import java.awt.image.BufferedImage;

public class ChunkImageGroovy {
    public final ChunkPos chunkPos;
    private DynamicTexture texture;
    private final World world;
    private final int yLevel;

    public ChunkImageGroovy(World world, ChunkPos chunkPos, int yLevel) {
        this.world = world;
        this.chunkPos = chunkPos;
        this.yLevel = yLevel;

        this.texture = new DynamicTexture(this.createImage());
    }

    public void bindTexture() {
        GlStateManager.bindTexture(this.texture.getGlTextureId());
    }

    public void dispose() {
        this.texture.deleteGlTexture();
    }

    private BufferedImage createImage() {
        int width = 16;
        int height = 16;

        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                BlockPos pos;
                int northY, westY;

                if (this.shouldDrawAtSameLayer()) {
                    pos = this.getFirstBlockGoingDown(this.chunkPos.getXStart() + x, this.yLevel + 1, this.chunkPos.getZStart() + z, 5);
                    northY = this.getFirstBlockGoingDown(this.chunkPos.getXStart() + x, this.yLevel + 1, this.chunkPos.getZStart() + z - 1, 6).getY();
                    westY = this.getFirstBlockGoingDown(this.chunkPos.getXStart() + x - 1, this.yLevel + 1, this.chunkPos.getZStart() + z, 6).getY();
                } else {
                    pos = new BlockPos(this.chunkPos.getXStart() + x, BananaMC.findHighestBlock(world, this.chunkPos.getXStart() + x, this.chunkPos.getZStart() + z, Blocks.AIR, Blocks.BEDROCK), this.chunkPos.getZStart() + z);
                    northY = BananaMC.findHighestBlock(world, pos.getX(), pos.getZ() - 1, Blocks.AIR, Blocks.BEDROCK);
                    westY = BananaMC.findHighestBlock(world, pos.getX() - 1, pos.getZ(), Blocks.AIR, Blocks.BEDROCK);
                }

                IBlockState state = this.world.getBlockState(pos);
                MapColor color = state.getMapColor(this.world, pos);
                int rgb = (color == null) ? MapColor.AIR.colorValue : color.colorValue;

                int red = ((rgb >> 16) & 255);
                int green = ((rgb >> 8) & 255);
                int blue = (rgb & 255);

                int biomeC = this.biomeColorFor(world, pos, state)

                if (biomeC != -1) {
                    float light = ((red + green + blue) / (255.0F * 3.0F)) + 0.25

                    red = Math.min(((biomeC >> 16) & 255) * light, 255);
                    green = Math.min(((biomeC >> 8) & 255) * light, 255);
                    blue = Math.min((biomeC & 255) * light, 255);
                }

                if ((pos.getY() > northY && northY >= 0) || (pos.getY() > westY && westY >= 0)) {
                    if (red == 0 && green == 0 && blue == 0) {
                        red = 3;
                        green = 3;
                        blue = 3;
                    } else {
                        if (red > 0 && red < 3) red = 3;
                        if (green > 0 && green < 3) green = 3;
                        if (blue > 0 && blue < 3) blue = 3;
                        red = Math.min((int) (red / 0.8), 255);
                        green = Math.min((int) (green / 0.8), 255);
                        blue = Math.min((int) (blue / 0.8), 255);
                    }
                }
                if ((pos.getY() < northY && northY >= 0) || (pos.getY() < westY && westY >= 0)) {
                    red = Math.max((int) (red * 0.8), 0);
                    green = Math.max((int) (green * 0.8), 0);
                    blue = Math.max((int) (blue * 0.8), 0);
                }

                image.setRGB(x, z, (255 << 24) | (red << 16) | (green << 8) | blue);
            }
        }

        return image;
    }

    private int biomeColorFor(World world, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.GRASS || block == Blocks.TALLGRASS || block == Blocks.WATERLILY || block == Blocks.REEDS || block == Blocks.CHORUS_PLANT || block == Blocks.DOUBLE_PLANT) {
            return world.getBiome(pos).getGrassColorAtPos(pos);
        }
        if (block == Blocks.LEAVES || block == Blocks.LEAVES2 || block == Blocks.VINE) {
            return world.getBiome(pos).getFoliageColorAtPos(pos);
        }
        if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
            return getWaterColorWithBiome(pos);
        }
        if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
            return 0xff8c00;
        }
        return -1;
    }

    private int getWaterColorWithBiome(BlockPos pos) {
        Biome biome = this.world.getBiome(pos);
        int tint = biome.getWaterColor();
        int BASE_WATER_COLOR = 0x3F76E4;
        float tr = ((tint >> 16) & 0xFF) / 255.0F;
        float tg = ((tint >> 8) & 0xFF) / 255.0F;
        float tb = (tint & 0xFF) / 255.0F;
        float avgTint = (tr + tg + tb) / 3.0F;
        int r = (int) (((BASE_WATER_COLOR >> 16) & 0xFF) * avgTint);
        int g = (int) (((BASE_WATER_COLOR >> 8) & 0xFF) * avgTint);
        int b = (int) ((BASE_WATER_COLOR & 0xFF) * avgTint);
        return (r << 16) | (g << 8) | b;
    }

    private BlockPos getFirstBlockGoingDown(int x, int y, int z, int maxTries) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
        int tries = 0;
        while (this.world.isAirBlock(pos) && ++tries < maxTries)
            pos.setY(pos.getY() - 1);

        return pos;
    }

    private boolean shouldDrawAtSameLayer() {
        return this.world.provider.isNether();
    }
}