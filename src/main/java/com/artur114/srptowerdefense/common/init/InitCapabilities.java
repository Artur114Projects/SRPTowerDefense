package com.artur114.srptowerdefense.common.init;

import com.artur114.bananalib.mc.cap.BananaCapStorage;
import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePre;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.IDamagedChunk;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.server.ServerDamagedChunk;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceManager;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

@AutoInstantiate
public class InitCapabilities implements ILoadStagePre {
    @CapabilityInject(IDamagedChunk.class)
    public static final Capability<IDamagedChunk> BLOCK_DAMAGE = null;
    @CapabilityInject(TowerDefenceEntity.class)
    public static final Capability<TowerDefenceEntity> TD_ENTITY_DATA = null;
    @CapabilityInject(TowerDefenceManager.class)
    public static final Capability<TowerDefenceManager> TOWER_DEFENCE_SYSTEM = null;

    @Override
    public void onPreInit() {
        CapabilityManager.INSTANCE.register(
            IDamagedChunk.class,
            new BananaCapStorage<>(),
            () -> new ServerDamagedChunk(null, null)
        );
        CapabilityManager.INSTANCE.register(
            TowerDefenceManager.class,
            new BananaCapStorage<>(),
            () -> new TowerDefenceManager(null)
        );
        CapabilityManager.INSTANCE.register(
            TowerDefenceEntity.class,
            new BananaCapStorage<>(),
            () -> new TowerDefenceEntity(null)
        );
    }
}
