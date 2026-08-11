package com.artur114.srptowerdefense.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.BananaASM;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import com.artur114.srptowerdefense.asm.ASMTransformerSRPTD;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ParasiteEventEntityTransformer extends AbstractASMTransformer {
    public ParasiteEventEntityTransformer() {
        super("com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity");
    }

    @Override
    public byte[] transform(IASMLogger logger, String className, byte[] bytecode) {
        ClassReader reader = new ClassReader(bytecode);
        ClassNodeAdv clazz = BananaASM.createClassNode(reader);
        this.transform(logger, className, clazz);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        clazz.accept(writer);
        return writer.toByteArray();
    }

    @Override
    protected ClassNodeAdv transform(IASMLogger logger, String className, ClassNodeAdv clazz) {
        clazz.findMethod("spawnNext").ifPresent(method -> {
            InsnBuilder insn = new InsnBuilder();
            insn.loadVars("A:0", "A:1");
            insn.invokeStatic(ASMTransformerSRPTD.HOOK_CLASS, "hookParasiteEvolved", "(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;)V");
            logger.info("Injecting patches into method {}.{}{}", className, method.name, method.desc);
            method.instructions.insert(insn.build());
        });
        return clazz;
    }

    @Override
    public int priority() {
        return 0;
    }
}
