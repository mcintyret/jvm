package com.mcintyret.jvm.test.atomicInt;

import com.mcintyret.jvm.test.BaseJvmTest;
import com.mcintyret.jvm.test.TestOutput;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.AssertJUnit.assertEquals;

@Test
public class ThreadTestWithAtomicIntTest extends BaseJvmTest {

    private static final int NUM_THREADS = 40;

    @Override
    protected void verifyResults(TestOutput output) {
        assertEquals(String.valueOf(NUM_THREADS), output.getStdOut());
    }

    public static class Test {
        public static void main(String[] args) {
            AtomicInteger ai = new AtomicInteger();
            final Random random = new Random();
            final CountDownLatch latch = new CountDownLatch(NUM_THREADS);
            for (int i = 0; i < NUM_THREADS; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100 + random.nextInt(50));
                        } catch (InterruptedException e) {
                            throw new AssertionError(e);
                        }
                        ai.getAndIncrement();
                        latch.countDown();
                    }
                }).start();
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }

            System.out.println(ai.get());
        }
    }
}
