package com.artur114.bananaasm.analyzer.operation;

import org.jetbrains.annotations.Nullable;

public class OVisitIntInsn extends OperationBase {
    private int operand = -1;
    protected OVisitIntInsn(int opcode, OperationWorkType type, @Nullable Runnable onWork) {
        super(opcode, type, onWork);
    }

    private OVisitIntInsn setOperand(int operand) {
        this.operand = operand;
        return this;
    }

    @Override
    public boolean visitIntInsn(int opcode, int operand) {
        return opcode == this.opcode && (this.operand == Integer.MIN_VALUE || operand == this.operand);
    }

    public static class Builder extends OperationBuilderBase<OVisitIntInsn, Builder> {
        private int operand = Integer.MIN_VALUE;

        @Override
        public Builder workType(OperationWorkType type) {
            this.type = type;
            return this;
        }

        @Override
        public Builder addOnWorkTask(Runnable task) {
            this.onWork = task;
            return this;
        }

        @Override
        public Builder startBuild(int opcode) {
            this.opcode = opcode;
            return this;
        }

        public Builder operand(int operand) {
            this.operand = operand;
            return this;
        }

        @Override
        public OVisitIntInsn build() {
            return new OVisitIntInsn(opcode, type, onWork).setOperand(this.operand);
        }
    }

}
