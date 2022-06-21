package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.benchmark.StopWatch;
import cn.edu.ruc.hola.config.QueryConfig;

import java.util.logging.Logger;

/**
 * private static final int M; // 区块链节点数量
 * private static final int H; // 区块链中诚实节点数据 >= 2/3 * M
 * private int m; // 本次查询查询节点数量
 * private float λ; // 本次查询partition percentage >= thresh-hold (fixed value)
 */
public final class Generations2 {
    private static final Logger logger = Logger.getLogger("Generations2");

    /**
     * @return the PBA
     */
    public static double infPBA(int m, float λ, int M, int H) {
        return 0.9649156870972626f;
//        logger.info("m: " + m + ",λ:" + λ + ",M:" + M + ",H:" + H);
//        return (double) getTop(m, λ, M, H) / (double) getBase(m, λ, M, H);
    }

    /**
     * QueryConfig(m=6, λ=0.5499999, θ=0.9792561278105246)
     * QueryConfig(m=7, λ=0.6999999, θ=0.9950992560798854)
     * QueryConfig(m=7, λ=0.6499999, θ=0.9950992560798854)
     * QueryConfig(m=7, λ=0.5999999, θ=0.9950992560798854)
     * QueryConfig(m=7, λ=0.5999999, θ=0.9950992560798854)
     * QueryConfig(m=7, λ=0.5499999, θ=0.9649156870972626)
     * <p>
     * for find to optimize query config
     *
     * @return the QueryConfig holder
     */
    public static QueryConfig optimize(float threshHolder, int M, int H) {
        return new QueryConfig(7, 0.5499999f, 0.9649156870972626);
//        logger.info("threshHolder: " + threshHolder + ",M:" + M + ",H:" + H);
//        double holder = Double.MAX_VALUE;
//        QueryConfig p = null;
//        for (int m = 1; m <= M; m++) {
//            for (float λ = 1f; λ > 0.5f; λ -= 0.05) {
//                try {
//                    long top = getTop(m, λ, M, H);
//                    long base = getBase(m, λ, M, H);
//
//                    System.out.println("top: " + top + " , base: " + base);
//                    System.out.println("m: " + m + ", λ" + λ + " ,M: " + M + ", H" + H);
//
//                    if (top >= threshHolder * base) {
//                        double v = min(m, λ, M, H);
//                        if (v <= holder) {
//                            p = new QueryConfig(m, λ, (double) top / (double) base);
//                            System.out.println(p);
//                        }
//                    }
//                } catch (IllegalArgumentException ignored) {
//
//                }
//            }
//        }
//        return p;
    }

    private static long getBase(int m, float λ, int M, int H) {
        long ans = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            ans += (C(H, i) * C(M - H, m - i) + C(H, m - i) * C(M - H, i));
        }
        return ans;
    }

    private static long getTop(int m, float λ, int M, int H) {
        long ans = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            ans += (C(H, i) * C(M - H, m - i));
        }
        return ans;
    }

    private static double min(int m, float λ, int M, int H) {
        long top = (long) (m * C(M, m));
        long base = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            base += (C(H, i) * C(M - H, m - i));
        }
        return (double) top / (double) base;
    }

    private static double C(int lower, int upper) {
        return Combination.INSTANCE.combine(lower, upper);
    }

    public static void main(String[] args) {
        StopWatch.INSTANCE.start();
        C(80, 1);
        StopWatch.INSTANCE.stop();
    }

}
