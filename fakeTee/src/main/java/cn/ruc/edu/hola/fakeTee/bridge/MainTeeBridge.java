package cn.ruc.edu.hola.fakeTee.bridge;


import cn.ruc.edu.hola.fakeTee.ocall.MainTeeOCall;
import cn.ruc.edu.hola.fakeTee.tee.MainTee;

interface IMainTeeECallBridge {

    void init(MainTeeOCall mainTeeOCall);

    String complexFilter(String data);

    String authMainTee(String data);
}

interface IMainTeeOCallBridge {
    void printString(String... text);
}

// delegate class , do not modify with other logic
public class MainTeeBridge implements IMainTeeECallBridge, IMainTeeOCallBridge {

    private final MainTee mainTee = new MainTee(); // delegate eCall

    private MainTeeOCall mainTeeOCall; // delegate oCall

    @Override
    public void init(MainTeeOCall mainTeeOCall) {
        // inject oCall
        this.mainTeeOCall = mainTeeOCall;
        // pass the bridge to the tee for oCall
        mainTee.inject(this);
    }

    @Override
    public String complexFilter(String data) {
        return mainTee.eCallComplexCurl(data);
    }

    @Override
    public String authMainTee(String data) {
        return mainTee.eCallAuth(data);
    }

    @Override
    public void printString(String... text) {
        mainTeeOCall.print(text);
    }
}
