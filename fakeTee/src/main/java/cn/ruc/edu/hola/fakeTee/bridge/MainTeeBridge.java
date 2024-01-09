package cn.ruc.edu.hola.fakeTee.bridge;


interface IMainTeeECallBridge {

    void init();

    String complexFilter(String data);

    String authMainTee(String data);
}

interface IMainTeeOCallBridge {

}

public class MainTeeBridge implements IMainTeeECallBridge, IMainTeeOCallBridge {


    @Override
    public void init() {
        // pass the bridge to the tee
    }

    @Override
    public String complexFilter(String data) {
        return "aaa";
    }

    @Override
    public String authMainTee(String data) {
        return "bbb";
    }
}
