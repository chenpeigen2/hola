package cn.ruc.edu.hola.client.test;

import cn.ruc.edu.hola.client.performance.TimedCyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TimedCyclicBarrierTest {
    public static void main(String[] args) {
        //一个私人旅行团包了一个导游（其实也可以没有导游），大家都相互熟悉，知道要等一下家人或者朋友
        //那么此时需要有个计数器，计数值设置为3

        TimedCyclicBarrier.BarrierRunnable runnable = new TimedCyclicBarrier.BarrierRunnable() {
            @Override
            public void run() {
                super.run();

            }
        };

        TimedCyclicBarrier cyclicBarrier = new TimedCyclicBarrier(20000, 20, runnable);
        runnable.setBarrier(cyclicBarrier);


        new Thread(() -> {
            try {
                System.out.println("A在群里发，我还要15millis");
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "到了，剩下俩也不知到来了没有调用await()等下吧");
            //有这个await()真方便我也不用关心他们到了没，都到了计数器就可以减为0就可以出发了
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            //阻塞结束后
            System.out.println("哦！都到了！A提着包说到");
        }).start();
        new Thread(() -> {
            try {
                System.out.println("B在群里发，我还要20millis");
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "到了，剩下俩也不知到来了没有调用await()等下吧");
            //有这个await()真方便我也不用关心他们到了没，都到了计数器就可以减为0就可以出发了
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            //阻塞结束后
            System.out.println("哦！都到了！B嘴里塞满了热狗支支吾吾说到");
        }).start();
        new Thread(() -> {
            try {
                System.out.println("C在群里发，我还要30millis");
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "到了，剩下俩也不知到来了没有调用await()等下吧");
            //有这个await()真方便我也不用关心他们到了没，都到了计数器就可以减为0就可以出发了
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            //阻塞结束后
            System.out.println("害！都到了，得亏你们用await()等我，我堵车了！C气喘吁吁说到");
        }).start();
    }
}
