package com.artur114.srptowerdefense.asm.transform;

import com.artur114.bananalib.asm.AbstractASMTransformer;
import com.artur114.bananalib.asm.tree.ClassNodeAdv;
import com.artur114.bananalib.asm.util.IASMLogger;
import com.artur114.bananalib.asm.util.InsnBuilder;
import com.artur114.srptowerdefense.asm.ASMTransformerSRPTD;

public class ParasiteEventEntityTransformer extends AbstractASMTransformer {
    public ParasiteEventEntityTransformer() {
        super("com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity");
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
