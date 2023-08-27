package cn.ruc.edu.hola.client.performance;

import cn.ruc.edu.hola.client.entity.ChaincodeRequest;

import java.util.List;

public abstract class Callback {

    public final List<ChaincodeRequest> chaincodeRequests;

    public abstract void onFinish(String text);


    public Callback(List<ChaincodeRequest> chaincodeRequests) {
        this.chaincodeRequests = chaincodeRequests;
    }

}
