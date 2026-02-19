package com.artur114.srptowerdefense.common.capabilities;

import com.artur114.srptowerdefense.common.blockdamage.IDamagedChunk;
import com.artur114.srptowerdefense.common.blockdamage.server.IServerDamagedChunk;
import com.artur114.srptowerdefense.common.blockdamage.server.ServerDamagedChunk;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class TowerDefenceCapabilities {
    @CapabilityInject(IDamagedChunk.class)
    public static final Capability<IDamagedChunk> BLOCK_DAMAGE = null;

    public static void preInit() {
        CapabilityManager.INSTANCE.register(IDamagedChunk.class, new Capability.IStorage<IDamagedChunk>() {
            @Override
            public NBTBase writeNBT(Capability<IDamagedChunk> capability, IDamagedChunk instance, EnumFacing side) {
                if (!(instance instanceof IServerDamagedChunk)) {
                    return null;
                }

                return ((IServerDamagedChunk) instance).serializeNBT();
            }

            @Override
            public void readNBT(Capability<IDamagedChunk> capability, IDamagedChunk instance, EnumFacing side, NBTBase nbt) {
                if (!(instance instanceof IServerDamagedChunk) || !(nbt instanceof NBTTagCompound)) {
                    return;
                }

                ((IServerDamagedChunk) instance).deserializeNBT((NBTTagCompound) nbt);
            }
        }, () -> new ServerDamagedChunk(null, null));
    }
}
