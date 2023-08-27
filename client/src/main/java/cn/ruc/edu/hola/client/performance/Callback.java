package cn.ruc.edu.hola.client.performance;

import cn.ruc.edu.hola.client.entity.Request;

import java.util.List;

public abstract class Callback {

    public final List<Request> requests;

    public abstract void onFinish(String text);


    public Callback(List<Request> requests) {
        this.requests = requests;
    }

}
