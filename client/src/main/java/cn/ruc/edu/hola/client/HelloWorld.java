package cn.ruc.edu.hola.client;

import cn.ruc.edu.hola.client.gateway.App;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("hello world");
        try {
            App.main(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
