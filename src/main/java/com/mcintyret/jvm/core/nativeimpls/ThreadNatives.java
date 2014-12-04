package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.core.thread.Thread;
import com.mcintyret.jvm.core.thread.Threads;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public enum ThreadNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }
    },
    CURRENT_THREAD("currentThread", "()Ljava/lang/Thread;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Thread thread = ctx.getThread();
            return NativeReturn.forReference(thread.getThisThread());
        }
    },
    START_0("start0", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            OopClass thread = Heap.getOopClass(args[0]);
            Thread nativeThread = Threads.get(thread);
            nativeThread.start();
            return NativeReturn.forVoid();
        }
    },
    INTERRUPT_0("interrupt0", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            OopClass thread = Heap.getOopClass(args[0]);
            Thread nativeThread = Threads.get(thread);
            nativeThread.interrupt();
            return NativeReturn.forVoid();
        }
    },
    SET_PRIORITY_0("setPriority0", "(I)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Threads.get(Heap.getOopClass(args[0])).getThread().setPriority(args[1]);
            return NativeReturn.forVoid();
        }
    },
    IS_ALIVE("isAlive", "()Z") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            boolean isAlive = Threads.get(Heap.getOopClass(args[0])).getThread().isAlive();
            return NativeReturn.forBool(isAlive);
        }
    },
    IS_INTERRUPTED("isInterrupted", "(Z)Z") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Thread thread = Threads.get(Heap.getOopClass(args[0]));
            return NativeReturn.forBool(thread.isInterrupted(args[0] > 0));
        }
    },SLEEP("sleep", "(J)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Thread thread = ctx.getThread();
            try {
                thread.sleep(Utils.toLong(args[0], args[1]));
                return NativeReturn.forVoid();
            } catch (InterruptedException e) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(e, thread));
            }
        }
    };

    private final MethodSignature methodSignature;

    private ThreadNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }

    @Override
    public String getClassName() {
        return "java/lang/Thread";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}

