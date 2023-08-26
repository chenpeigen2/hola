package cn.ruc.edu.hola.client.performance;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

@Deprecated
public class BackgroudThread extends Thread {
    public static Thread INSTANCE;

    static {
        INSTANCE = new BackgroudThread();
    }

    private BackgroudThread() {
    }

    public volatile Queue<Package> queue = new ArrayDeque<>();

    public volatile Set<Package> orderset = new HashSet<>();

    public synchronized boolean submit(Package cargo) {
        if (check()) {
            internRun();
        }
        queue.offer(cargo);
        orderset.add(cargo);
        return true;
    }

    private synchronized boolean check() {
        return queue.size() >= 10;
    }

    public synchronized boolean isFinish(Package cargo) {
        return !orderset.contains(cargo);
    }

    private void internRun() {
        int sz = queue.size();

        // execute your own string text
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println("777777");

        for (int i = 0; i < sz; i++) {
            Package cargo = queue.poll();

            orderset.remove(cargo);
        }
    }

    @Override
    public void run() {
        for (; ; ) {
            if (check()) {
                internRun();
            }
        }
    }


    public static void main(String[] args) {
        INSTANCE.start();

    }
}
