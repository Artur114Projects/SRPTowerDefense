package com.artur114.srptowerdefense.common.capabilities;

import com.artur114.srptowerdefense.common.systems.blockdamage.IDamagedChunk;
import com.artur114.srptowerdefense.common.systems.blockdamage.server.IServerDamagedChunk;
import com.artur114.srptowerdefense.common.systems.blockdamage.server.ServerDamagedChunk;
import com.artur114.srptowerdefense.common.systems.parasitewaves.WavesManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class TowerDefenceCapabilities {
    @CapabilityInject(IDamagedChunk.class)
    public static final Capability<IDamagedChunk> BLOCK_DAMAGE = null;
    @CapabilityInject(WavesManager.class)
    public static final Capability<WavesManager> WAVES_SYSTEM = null;


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
        CapabilityManager.INSTANCE.register(WavesManager.class, new Capability.IStorage<WavesManager>() {
            @Override
            public NBTBase writeNBT(Capability<WavesManager> capability, WavesManager instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<WavesManager> capability, WavesManager instance, EnumFacing side, NBTBase nbt) {
                if (!(nbt instanceof NBTTagCompound)) {
                    return;
                }

                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, () -> new WavesManager(null));

    }
}
