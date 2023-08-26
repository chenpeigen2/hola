package cn.ruc.edu.hola.client.performance;

import java.util.concurrent.*;

public class TimedCyclicBarrier extends CyclicBarrier {
    private final long loopTime;

    private final CyclicBarrier barrier;

    private volatile boolean checked = false;

    private long currentTime = -1;

    private Thread checkThread;

    public TimedCyclicBarrier(long loopTime, int parties, Runnable barrierAction) {
        super(parties, barrierAction);
        this.loopTime = loopTime;
        barrier = new CyclicBarrier(parties, barrierAction);
    }

    // runnable when barrier was release
    public static abstract class BarrierRunnable implements Runnable {

        private final TimedCyclicBarrier barrier;

        public BarrierRunnable(TimedCyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            System.out.println();
            barrier.reset();
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
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            // DO NOTHING
        }
    }


    private void runFakeThread() {
        int vacantThread = getParties() - getNumberWaiting();
        for (int i = 0; i < vacantThread; i++) {
            new InnerThread(barrier).start();
        }
    }


    @Override
    public synchronized int await() throws InterruptedException, BrokenBarrierException {
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

    @Override
    public int await(long timeout, TimeUnit unit) throws InterruptedException, BrokenBarrierException, TimeoutException {
        // NO IMPLEMENTATION
        return -1;
    }

    @Override
    public int getNumberWaiting() {
        return barrier.getNumberWaiting();
    }

    @Override
    public int getParties() {
        return barrier.getParties();
    }

    @Override
    public boolean isBroken() {
        return barrier.isBroken();
    }

    @Override
    public void reset() {
        resetInternal();
        barrier.reset();
    }

    private void resetInternal() {
        checkThread.stop(); // stop the thread
        checked = false;
    }
}
