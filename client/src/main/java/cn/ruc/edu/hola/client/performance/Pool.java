package cn.ruc.edu.hola.client.performance;

import java.util.concurrent.*;

@Deprecated
public class Pool {


    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();


//    public static Future submit(Package cargo) {
//        return executorService.submit(cargo);
//    }
}
