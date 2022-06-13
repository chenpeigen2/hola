package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.config.model.Pair;

import java.util.logging.Logger;

/**
 * private static final int M; // 区块链节点数量
 * private static final int H; // 区块链中诚实节点数据 >= 2/3 * M
 * private int m; // 本次查询查询节点数量
 * private float λ; // 本次查询partition percentage >= thresh-hold (fixed value)
 */
public final class Generations2 {

    private static final int MULTIPLY = 10;

    private static final Logger logger = Logger.getLogger("Generations2");

    /**
     * @return the PBA
     */
    public static double infPBA(int m, float λ, int M, int H) {
        M *= MULTIPLY;
        H *= MULTIPLY;
        logger.info("m: " + m + ",λ:" + λ + ",M:" + M + ",H:" + H);
        return (double) getTop(m, λ, M, H) / (double) getBase(m, λ, M, H);
    }

    /**
     * @return the pair holder
     */
    public static Pair optimize(float threshHolder, int M, int H) {
        logger.info("threshHolder: " + threshHolder + ",M:" + M + ",H:" + H);
        double holder = Double.MAX_VALUE;
        Pair p = null;
        for (int m = 1; m <= M; m++) {
            for (float λ = 1f; λ > 0.5f; λ -= 0.05) {
                try {
                    long top = getTop(m, λ, M, H);
                    long base = getBase(m, λ, M, H);
                    if (top >= threshHolder * base) {
                        double v = min(m, λ, M, H);
                        if (v <= holder) {
                            p = new Pair(m, λ, (double) top / (double) base);
                        }
                    }
                } catch (IllegalArgumentException ignored) {

                }
            }
        }
        return p;
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

    private static float C(int lower, int upper) {
        float up = NumPlex(lower);
        long low = NumPlex(upper) * NumPlex(lower - upper);
        return up / low;
    }

    /**
     * 普通的循环方法求阶乘
     */
    private static long NumPlex(int num) {
        long sum = 1;
        if (num < 0) {
            throw new IllegalArgumentException("需要计算的参数必须为正数！");//抛出不合理参数异常
        }
        for (int i = 1; i <= num; i++) {
            sum *= i;
        }
        return sum;
    }
}
