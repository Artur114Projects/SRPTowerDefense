package com.artur114.bananalib.asm.base;

import org.objectweb.asm.MethodVisitor;

public interface IMVInstance {
    MethodVisitor getInstance(MethodVisitor mv);
    String[] getTargets();
}
