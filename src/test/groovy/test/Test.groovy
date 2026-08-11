package test

import com.artur114.srptowerdefense.asm.ASMHookSRPTD
import com.artur114.srptowerdefense.common.worldstate.blockdamage.ExtendedDamageStorageMapped
import com.dhanantry.scapeandrunparasites.init.SRPBlocks
import net.minecraft.nbt.NBTTagCompound
import org.objectweb.asm.Type

class Test {
    static void main(String[] args) {
        SRPBlocks.declaredFields.each {
            if (!it.name.toLowerCase().contains("gore".toLowerCase())) {
                return
            }

            println "BlockMetaRegistry.bindResistanceMul(SRPBlocks.${it.name}, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.5F);"
        }
    }

    static void printDesc(Class<?> clazz, String name) {
        println Type.getMethodDescriptor(clazz.declaredMethods.find{it.name == name})
    }
}
