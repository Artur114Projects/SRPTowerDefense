package com.artur114.srptowerdefense.asm;

import com.artur114.bananalib.mc.cap.BananaCaps;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceEntity;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;

import java.util.Optional;

public class ASMHookSRPTD {
    public static boolean nookDoMelt(EntityParasiteBase entity) {
        Optional<TowerDefenceEntity> data = BananaCaps.capability(entity, InitCapabilities.TD_ENTITY_DATA);
        return !data.isPresent() || !data.get().isBindToTDObj();
    }

    public static void hookParasiteEvolved(EntityParasiteBase entityIn, EntityParasiteBase entityOut) {
        BananaCaps.capability(entityIn, InitCapabilities.TD_ENTITY_DATA).ifPresent(data -> data.onEvolved(entityOut));
    }
}
