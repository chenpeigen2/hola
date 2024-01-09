package cn.ruc.edu.hola.fakeTee.bridge;


import cn.ruc.edu.hola.fakeTee.ocall.IMainTeeOCall;
import cn.ruc.edu.hola.fakeTee.tee.IMainTeeECall;

// delegate class , do not modify with other logic
public class MainTeeBridge implements IMainTeeECallBridge, IMainTeeOCallBridge {

    private final IMainTeeECall mainTeeECall = IMainTeeECall.getInstance(); // delegate eCall

    private final IMainTeeOCall mainTeeOCall = IMainTeeOCall.getInstance(); // delegate oCall

    public void init() {
        // pass the bridge to the tee for oCall
        mainTeeECall.inject(this);
    }

    @Override
    public String complexFilter(String data) {
        return mainTeeECall.eCallComplexCurl(data);
    }

    @Override
    public String authMainTee(String data) {
        return mainTeeECall.eCallAuth(data);
    }

    @Override
    public void printString(String... text) {
        mainTeeOCall.print(text);
    }
}
