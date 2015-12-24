package com.mcintyret.jvm.test;

import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.AssertJUnit.assertEquals;

@Test
public class ThreadTestWithAtomicIntTest {

    private static final int NUM_THREADS = 40;

    public void runTest() {
        String output = JvmTestUtils.runTest("ThreadTestWithAtomicIntTest").trim();

        assertEquals(output, String.valueOf(NUM_THREADS));
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
