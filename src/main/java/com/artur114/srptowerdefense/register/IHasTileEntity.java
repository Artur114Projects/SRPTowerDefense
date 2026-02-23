package com.artur114.srptowerdefense.register;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntity<T extends TileEntity> {
    Class<T> tileEntityClass();
}
