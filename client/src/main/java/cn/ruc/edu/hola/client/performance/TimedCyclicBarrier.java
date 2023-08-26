package cn.ruc.edu.hola.client.performance;

import java.util.concurrent.*;

public final class TimedCyclicBarrier {
    private final long loopTime;

    private final CyclicBarrier barrier;

    private volatile boolean checked = false;

    private long currentTime = -1;

    private Thread checkThread;

    public TimedCyclicBarrier(long loopTime, int parties, BarrierRunnable barrierAction) {
        this.loopTime = loopTime;
        barrier = new CyclicBarrier(parties, barrierAction);
    }

    // runnable when barrier was release
    public static abstract class BarrierRunnable implements Runnable {

        private TimedCyclicBarrier barrier;

        public void setBarrier(TimedCyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            System.out.println("导游：都到齐了！出发！青春没有售价！我们直飞拉萨，带着A,B,C产生的结果出发了！");
        }
    }

    private static class InnerThread extends Thread {

        private final CyclicBarrier barrier;

        public InnerThread(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "waitinggggggggggggg");
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            // DO NOTHING
            System.out.println(Thread.currentThread().getName() + "我们这些凑数的也都到了");
        }
    }


    private void runFakeThread() {
        int vacantThread = getParties() - getNumberWaiting();
        for (int i = 0; i < vacantThread; i++) {
            new InnerThread(barrier).start();
        }
    }

    public int await() throws InterruptedException, BrokenBarrierException {
        // produce fake thread
        if (!checked) {
            currentTime = System.currentTimeMillis();
            checkThread = new Thread(() -> {
                for (; ; ) {
                    // means time out
                    if (System.currentTimeMillis() > currentTime + loopTime) {
                        runFakeThread();
                        break;
                    }
                    // means already have enough thread
                    if (getNumberWaiting() == getParties()) {
                        break;
                    }
                }
            });
            checkThread.start();
            checked = true;
        }

        return barrier.await();
    }

    public int getNumberWaiting() {
        return barrier.getNumberWaiting();
    }

    public int getParties() {
        return barrier.getParties();
    }

    public boolean isBroken() {
        return barrier.isBroken();
    }

    public void reset() {
        resetInternal();
        barrier.reset();
    }

    private void resetInternal() {
        checkThread.stop(); // stop the thread
        checked = false;
    }
}
