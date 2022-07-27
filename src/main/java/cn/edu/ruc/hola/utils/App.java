package cn.edu.ruc.hola.utils;

import okhttp3.*;

import java.io.IOException;

public class App {
    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getId());
        System.out.println("==============");
        OkHttpClient okHttpClient = new OkHttpClient();//1.定义一个client
        Request request = new Request.Builder().url("http://www.baidu.com").build();//2.定义一个request
        Call call = okHttpClient.newCall(request);//3.使用client去请求
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(Thread.currentThread());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();//5.获得网络数据
                System.out.println(result);

                System.out.println(Thread.currentThread().getId());
            }
        });


        Call call1 = okHttpClient.newCall(request);//3.使用client去请求
        call1.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(Thread.currentThread());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();//5.获得网络数据
                System.out.println(result);

                System.out.println(Thread.currentThread().getId());
            }
        });

    }
}
