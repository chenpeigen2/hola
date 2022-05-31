package cn.edu.ruc.hola.utils;

import lombok.Data;
import cn.edu.ruc.hola.config.model.Pair;

@Data
@Deprecated
public final class Generations {
    private static final int M; // 区块链节点数量
    private static final int H; // 区块链中诚实节点数据 >= 2/3 * M

    static {
        M = 26;
        H = 20;
    }

    private int m; // 本次查询查询节点数量
    private float λ; // 本次查询partition percentage >= thresh-hold (fixed value)

    public Generations() {
    }

    public Generations(int m, float λ) {
        this.m = m;
        this.λ = λ;
    }

    public static float C(int lower, int upper) {
        float up = NumPlex(lower);
        long low = NumPlex(upper) * NumPlex(lower - upper);
        return up / low;
    }

    /**
     * 普通的循环方法求阶乘
     */
    public static long NumPlex(int num) {
        long sum = 1;
        if (num < 0) {
            throw new IllegalArgumentException("需要计算的参数必须为正数！");//抛出不合理参数异常
        }
        for (int i = 1; i <= num; i++) {
            sum *= i;
        }
        return sum;
    }

    private long getTop() {
        long ans = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            ans += (C(H, i) * C(M - H, m - i));
        }
        return ans;
    }

    private long getBase() {
        long ans = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            ans += (C(H, i) * C(M - H, m - i) + C(H, m - i) * C(M - H, i));
        }
        return ans;
    }

    /**
     * @return the PBA
     */
    public double infPBA() {
        return (double) getTop() / (double) getBase();
    }

    /******************** through thresh hold to get best m or λ **********************/
    private long getBase(int m, float λ) {
        long ans = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            ans += (C(H, i) * C(M - H, m - i) + C(H, m - i) * C(M - H, i));
        }
        return ans;
    }

    private long getTop(int m, float λ) {
        long ans = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            ans += (C(H, i) * C(M - H, m - i));
        }
        return ans;
    }

    public double min(int m, float λ) {
        long top = (long) (m * C(M, m));
        long base = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            base += (C(H, i) * C(M - H, m - i));
        }
        return (double) top / (double) base;
    }

    public Pair optimize(float threshHolder) {
        double holder = Double.MAX_VALUE;
        Pair p = null;
        for (int m = 1; m <= M; m++) {
            for (float λ = 1f; λ > 0.5f; λ -= 0.05) {
                try {
                    long top = getTop(m, λ);
                    long base = getBase(m, λ);
                    if (top >= threshHolder * base) {
                        double v = min(m, λ);
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
}
