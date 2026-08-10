package com.artur114.srptowerdefense.common.tileentity;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.base.BTileBase;
import com.artur114.bananalib.mc.base.tileabs.ITileBlockBreakListener;
import com.artur114.bananalib.mc.base.tileabs.ITileBlockPlaceListener;
import com.artur114.bananalib.mc.base.tileabs.ITileBlockUseListener;
import com.artur114.bananalib.mc.cap.BananaCaps;
import com.artur114.bananalib.mc.math.m2d.vec.PosMc2I;
import com.artur114.bananalib.mc.nbt.BananaAutoNBT;
import com.artur114.bananalib.mc.nbt.auto.AutoNBTEntry;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.artur114.srptowerdefense.common.network.client.CPacketAreaProtector;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ITowerDefenceObject;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ProtectedZone;
import com.artur114.srptowerdefense.main.SRPTDMain;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityAreaProtector extends BTileBase implements ITileBlockUseListener, ITileBlockBreakListener {
    @AutoNBTEntry
    private boolean active = false;
    @AutoNBTEntry
    private int tdObjId = 0;
    @AutoNBTEntry
    private int range = 5;
    @Nullable
    private ProtectedZone zone = null;


    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (this.active && this.zone != null) {
            this.zone.explode();
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            CPacketAreaProtector.sendOpenGui((EntityPlayerMP) playerIn, this.pos, this.zone != null ? this.zone.protectedChunks() : new long[0], this.active, this.range);
        }
        return true;
    }

    @Override
    public void onLoad() {
        if (this.active && !this.world.isRemote) {
            BananaCaps.capability(this.world, InitCapabilities.TOWER_DEFENCE_SYSTEM).ifPresent(manager -> {
                ITowerDefenceObject obj = manager.tdObjFromId(this.tdObjId);

                if (obj instanceof ProtectedZone) {
                    this.zone = (ProtectedZone) obj;
                }
            });
        }
    }

    public void messageFromGui(EntityPlayerMP sender, NBTTagCompound nbt) {
        switch (nbt.getInteger("action")) {
            case 0:
                this.range = MathHelper.clamp(nbt.getInteger("range"), 2, 10);
                break;
            case 1:
                if (!this.active && this.canActivate()) {
                    this.active = true;
                    this.onActivate();
                    CPacketAreaProtector.sendAcceptActivateRequest(sender);
                }
                break;
            case 2:
                if (this.active && this.zone != null) {
                    ChunkPos pos = BananaMC.chunkPosFromLong(nbt.getLong("chunk"));
                    boolean state = nbt.getBoolean("state");
                    if (this.zone.doProtect(pos, state)) {
                        CPacketAreaProtector.sendAcceptProtectRequest(sender, pos, state);
                    }
                }
                break;
        }
    }

    private boolean canActivate() {
        return this.pos.getY() >= 64 && this.pos.getY() <= 64 + 16;
    }

    private void onActivate() {
        BananaCaps.capability(this.world, InitCapabilities.TOWER_DEFENCE_SYSTEM).ifPresent(manager -> {
            manager.addObject(this.zone = new ProtectedZone(this.pos), this.tdObjId = manager.createSafeId());
        });
    }

    @Override
    protected void readSyncNBT(NBTTagCompound nbt) {}

    @Override
    protected NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {return nbt;}

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        return super.writeToNBT(BananaAutoNBT.writeToNBT(this, nbt));
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound nbt) {
        BananaAutoNBT.readFromNBT(this, nbt);
        super.readFromNBT(nbt);
    }
}
