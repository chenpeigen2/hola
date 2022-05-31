package cn.edu.ruc.hola.utils;

import java.util.Arrays;
import java.util.Random;

public final class ShuffleUtils {

    public static String[] randomChoose(String[] ids, int cnt) {
        shuffle(ids);
        return Arrays.copyOf(ids, cnt);
    }


    private static <T> void swap(T[] a, int i, int j) {
        T temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static <T> void shuffle(T[] arr) {
        int length = arr.length;
        Random rand = new Random(); // init the random to fit the random
        for (int i = length; i > 0; i--) {
            int randInd = rand.nextInt(i);
            swap(arr, randInd, i - 1);
        }
    }
}
