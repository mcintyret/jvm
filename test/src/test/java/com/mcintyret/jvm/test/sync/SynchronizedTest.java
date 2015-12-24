package com.mcintyret.jvm.test.sync;

import com.mcintyret.jvm.test.BaseJvmTest;
import com.mcintyret.jvm.test.TestOutput;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static org.testng.AssertJUnit.assertEquals;

public class SynchronizedTest extends BaseJvmTest {

    private static final int NUM_THREADS = 35;

    @Override
    protected void verifyResults(TestOutput output) {
        assertEquals(String.valueOf(NUM_THREADS), output.getStdOut());
    }

    public static class Test {
        public static void main(String[] args) {
            final int[] a = new int[1];
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
                        synchronized (a) {
                            a[0]++;
                        }
                        latch.countDown();
                    }
                }).start();
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }

            System.out.println(a[0]);
        }
    }

}
