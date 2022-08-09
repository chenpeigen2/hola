package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.config.QueryConfig;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * private static final int M; // 区块链节点数量
 * private static final int H; // 区块链中诚实节点数据 >= 2/3 * M
 * private int m; // 本次查询查询节点数量
 * private float λ; // 本次查询partition percentage >= thresh-hold (fixed value)
 */
public final class Generations2 {
    private Generations2() {
    }

    private static final Map<Integer, ConfigCache> cacheMap = new HashMap<>();

    private static int hash(float threshHolder, int M, int H) {
        return Objects.hash(threshHolder, M, H);
    }

    private static ConfigCache getCacheConfig(float threshHolder, int M, int H) {
        int mHashCode = hash(threshHolder, M, H);
        return cacheMap.computeIfAbsent(mHashCode, k -> new ConfigCache(threshHolder, M, H));
    }

    private static class ConfigCache {
        private final float threshHolder;
        private final int M;
        private final int H;

        private QueryConfig config;

        public ConfigCache(float threshHolder, int m, int h) {
            this.threshHolder = threshHolder;
            M = m;
            H = h;
        }

        public QueryConfig getConfig() {
            return config;
        }

        public void setConfig(QueryConfig config) {
            this.config = config;
        }

        public float getThreshHolder() {
            return threshHolder;
        }

        public int getM() {
            return M;
        }

        public int getH() {
            return H;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConfigCache that = (ConfigCache) o;
            return Float.compare(that.threshHolder, threshHolder) == 0 && M == that.M && H == that.H;
        }

        @Override
        public int hashCode() {
            return Objects.hash(threshHolder, M, H);
        }

        @Override
        public String toString() {
            return "ConfigHolder{" + "threshHolder=" + threshHolder + ", M=" + M + ", H=" + H + '}';
        }
    }

    /**
     * use it when return the result and calculate the trust rate
     *
     * @return to evaluate
     */
    public static double evaluate(int m, float λ, int M, int H) {
        return getTop(m, λ, M, H) / getBase(m, λ, M, H);
    }


    private static boolean shouldAccept(int m, float λ, int M, int H, double expected) {
        double top = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            top += C(H, i) * C(M - H, m - i);
        }
        double base = C(M, m);
        return top / base > expected;
    }

    /**
     * QueryConfig(m=6, λ=0.5499999, θ=0.9792561278105246)
     * QueryConfig(m=7, λ=0.6999999, θ=0.9950992560798854)
     * QueryConfig(m=7, λ=0.6499999, θ=0.9950992560798854)
     * QueryConfig(m=7, λ=0.5999999, θ=0.9950992560798854)
     * QueryConfig(m=7, λ=0.5999999, θ=0.9950992560798854)
     * QueryConfig(m=7, λ=0.5499999, θ=0.9649156870972626)
     * <p>
     * optimize code for test
     * <p>
     * QueryConfig(m=20, λ=0.5499999, θ=0.9999199897565545)
     * <p>
     * for find to optimize query config
     *
     * @return the QueryConfig holder
     */
    public static QueryConfig select(float threshHolder, int M, int H, float expected) {
        ConfigCache configCache = getCacheConfig(threshHolder, M, H);
        if (configCache.getConfig() != null) return configCache.getConfig();
        double holder = Double.MAX_VALUE;
        QueryConfig p = null;
        // start node should >= 1 , and select node should less than M/3
        // default set the λ start as 0.75 3/4
        for (int m = 2; m <= M / 3; m++) {
            for (float λ = 1f; λ > 0.5f; λ -= 0.01) {
                double top = getTop(m, λ, M, H);
                double base = getBase(m, λ, M, H);
                if (top >= threshHolder * base) {
                    double v = min(m, λ, M, H);
                    if (v <= holder && shouldAccept(m, λ, M, H, expected)) {
                        holder = v;
                        p = new QueryConfig(m, λ, top / base);
                    }
                }
            }
        }
        configCache.setConfig(p);
        return p;
    }

    private static double getBase(int m, float λ, int M, int H) {
        double ans = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            ans += (C(H, i) * C(M - H, m - i) + C(H, m - i) * C(M - H, i));
        }
        return ans;
    }

    private static double getTop(int m, float λ, int M, int H) {
        double ans = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            ans += (C(H, i) * C(M - H, m - i));
        }
        return ans;
    }

    private static double min(int m, float λ, int M, int H) {
        double top = m * C(M, m);
        double base = 0;
        for (int i = (int) Math.ceil(λ * m); i <= m; i++) {
            base += (C(H, i) * C(M - H, m - i));
        }
        return top / base;
    }

    public static double C(int lower, int upper) {
        return CombinatoricsUtils.binomialCoefficientDouble(lower, upper);
    }

}