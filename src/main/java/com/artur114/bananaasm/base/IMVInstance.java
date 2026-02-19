package com.artur114.bananaasm.base;

import org.objectweb.asm.MethodVisitor;

public interface IMVInstance {
    MethodVisitor getInstance(MethodVisitor mv);
    String[] getTargets();
}
