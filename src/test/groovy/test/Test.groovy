package test

import com.artur114.srptowerdefense.asm.ASMHookSRPTD
import org.objectweb.asm.Type

class Test {
    static void main(String[] args) {
        printDesc(ASMHookSRPTD, "hookParasiteEvolved")
    }

    static void printDesc(Class<?> clazz, String name) {
        println Type.getMethodDescriptor(clazz.declaredMethods.find{it.name == name})
    }
}
