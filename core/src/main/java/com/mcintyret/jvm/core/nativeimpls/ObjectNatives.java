package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.ImportantClasses;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import org.reflections.Reflections;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public enum ObjectNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Reflections reflections = new Reflections("com.mcintyret.jvm.core.nativeimpls");
            Set<Class<? extends NativeImplementation>> classes = reflections.getSubTypesOf(NativeImplementation.class);

            for (Class<? extends NativeImplementation> clazz : classes) {
                if (clazz.isEnum() && clazz != ObjectNatives.class) {
                    NativeImplementationRegistry.registerNatives(clazz);
                }
            }
            return NativeReturn.forVoid();
        }
    },
    WAIT("wait", "(J)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            long timeout = args.getLong(1);
            Condition condition = args.getOop(0).getMarkRef().getMonitorCondition();
            try {
                Heap.threadSleeping();
                if (timeout == 0) {
                    condition.await();
                } else {
                    condition.await(timeout, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e) {
                Thread.interrupted();
            } finally {
                Heap.threadWaking();
            }
            return NativeReturn.forVoid();
        }
    },
    NOTIFY("notify", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            args.getOop(0).getMarkRef().getMonitorCondition().signal();
            return NativeReturn.forVoid();
        }
    },
    NOTIFY_ALL("notifyAll", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            args.getOop(0).getMarkRef().getMonitorCondition().signalAll();
            return NativeReturn.forVoid();
        }
    },
    HASHCODE("hashCode", "()I") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forInt(args.getRawValue(0));
        }
    },
    CLONE("clone", "()Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Oop oop = args.getOop(0);

            Oop clone;
            if (oop instanceof OopArray) {
                OopArray array = (OopArray) oop;
                clone = array.getClassObject().newArray(array.getLength());
            } else {
                clone = ((OopClass) oop).getClassObject().newObject();
            }

            Variables.copyInto(oop.getFields(), clone.getFields());

            return NativeReturn.forReference(clone);
        }
    },
    GET_CLASS("getClass", "()Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forReference(args.getOop(0).getClassObject().getOop());
        }
    };

    private final MethodSignature methodSignature;

    private ObjectNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return ImportantClasses.JAVA_LANG_OBJECT;
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

    public static void registerNatives() {
        NativeImplementationRegistry.registerNatives(ObjectNatives.class);
    }
}
