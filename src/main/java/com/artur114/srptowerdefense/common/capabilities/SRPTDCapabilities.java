package com.artur114.srptowerdefense.common.capabilities;

import com.artur114.srptowerdefense.common.systems.blockdamage.IDamagedChunk;
import com.artur114.srptowerdefense.common.systems.blockdamage.server.IServerDamagedChunk;
import com.artur114.srptowerdefense.common.systems.blockdamage.server.ServerDamagedChunk;
import com.artur114.srptowerdefense.common.systems.towerdefence.TowerDefenceManager;
import com.artur114.srptowerdefense.common.systems.towerdefence.TowerDefenceEntity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class SRPTDCapabilities {
    @CapabilityInject(IDamagedChunk.class)
    public static final Capability<IDamagedChunk> BLOCK_DAMAGE = null;
    @CapabilityInject(TowerDefenceEntity.class)
    public static final Capability<TowerDefenceEntity> TD_ENTITY_DATA = null;
    @CapabilityInject(TowerDefenceManager.class)
    public static final Capability<TowerDefenceManager> TOWER_DEFENCE_SYSTEM = null;


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
        CapabilityManager.INSTANCE.register(TowerDefenceManager.class, new Capability.IStorage<TowerDefenceManager>() {
            @Override
            public NBTBase writeNBT(Capability<TowerDefenceManager> capability, TowerDefenceManager instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<TowerDefenceManager> capability, TowerDefenceManager instance, EnumFacing side, NBTBase nbt) {
                if (!(nbt instanceof NBTTagCompound)) {
                    return;
                }

                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, () -> new TowerDefenceManager(null));
        CapabilityManager.INSTANCE.register(TowerDefenceEntity.class, new Capability.IStorage<TowerDefenceEntity>() {
            @Override
            public NBTBase writeNBT(Capability<TowerDefenceEntity> capability, TowerDefenceEntity instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<TowerDefenceEntity> capability, TowerDefenceEntity instance, EnumFacing side, NBTBase nbt) {
                if (!(nbt instanceof NBTTagCompound)) {
                    return;
                }

                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, () -> new TowerDefenceEntity(null));
    }
}
