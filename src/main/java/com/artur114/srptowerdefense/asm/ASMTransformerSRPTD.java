package com.artur114.srptowerdefense.asm;

import com.artur114.bananalib.asm.ASMTransformBus;
import com.artur114.srptowerdefense.asm.transform.*;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;

public class ASMTransformerSRPTD implements IClassTransformer {
    public static final String HOOK_CLASS = "com/artur114/srptowerdefense/asm/ASMHookSRPTD";
    private final ASMLoggerLog4j logger = new ASMLoggerLog4j(LogManager.getLogger("SRPTD/ASM"));
    private final ASMTransformBus bus = new ASMTransformBus();

    public ASMTransformerSRPTD() {
        this.bus.registerTransformer(
            new EntityInfAllTransformer(),
            new ParasiteEventEntityTransformer()
        );
        this.bus.registerDownListener(((tr, e) -> {
            logger.error("An exception occurred in transformer {}", tr, e);
        }));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return this.bus.transform(this.logger, transformedName, basicClass);
    }
}
