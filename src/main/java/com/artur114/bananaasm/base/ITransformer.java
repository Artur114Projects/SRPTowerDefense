package com.artur114.bananaasm.base;


public interface ITransformer {
    default boolean isTarget(String transformedName) {return transformedName.equals(this.getTarget());}
    byte[] transform(String name, String transformedName, byte[] basicClass) throws Exception;
    String getTarget();
}
