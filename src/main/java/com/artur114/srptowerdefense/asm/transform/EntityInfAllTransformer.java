package com.artur114.srptowerdefense.asm.transform;

import com.artur114.bananalib.asm.BananaASM;
import com.artur114.bananalib.asm.IASMTransformer;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import com.artur114.bananalib.asm.util.InsnCodes;
import com.artur114.srptowerdefense.asm.ASMTransformerSRPTD;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class EntityInfAllTransformer implements IASMTransformer, InsnCodes {

    protected void transform(IASMLogger log, String className, ClassNodeAdv clazz) {
        clazz.findMethod("melt").ifPresent(method -> {
            InsnBuilder insn = new InsnBuilder();
            insn.loadVars("A:0");
            insn.invokeStatic(ASMTransformerSRPTD.HOOK_CLASS, "nookDoMelt", "(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;)Z");
            insn.ifFalseReturn(RETURN);
            log.info("Injecting patches into method {}.{}{}", className, method.name, method.desc);
            method.instructions.insert(insn.build());
        });
    }

    @Override
    public byte[] transform(IASMLogger logger, String className, byte[] bytecode) {
        ClassReader reader = new ClassReader(bytecode);
        ClassNodeAdv clazz = BananaASM.createClassNode(reader);
        this.transform(logger, className, clazz);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES) {
            @Override
            protected String getCommonSuperClass(String type1, String type2) {
                return "java/lang/Object";
            }
        };
        clazz.accept(writer);
        return writer.toByteArray();
    }

    @Override
    public boolean isTarget(String className) {
        return className.contains("com.dhanantry.scapeandrunparasites.entity.monster.infected");
    }

    @Override
    public int priority() {
        return 0;
    }
}
