package com.artur114.srptowerdefense.common.util.interfaces;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface IWriteToNBT {
    NBTTagCompound writeToNBT(NBTTagCompound nbt);

    static <T extends IReadFromNBT> List<T> initObjectsAsNBTList(NBTTagList list, Class<T> objClass) throws NullPointerException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (list == null || objClass == null) {
            throw new NullPointerException();
        }

        List<T> ret = new ArrayList<>(list.tagCount());

        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            T read = objClass.newInstance();
            read.readFromNBT(tag);
            ret.add(read);
        }

        return ret;
    }

    static NBTTagList objectsToNBT(Collection<? extends IWriteToNBT> objects) {
        NBTTagList list = new NBTTagList();

        for (IWriteToNBT obj : objects) {
            list.appendTag(obj.writeToNBT(new NBTTagCompound()));
        }

        return list;
    }
}
