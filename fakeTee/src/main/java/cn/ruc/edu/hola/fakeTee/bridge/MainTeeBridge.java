package cn.ruc.edu.hola.fakeTee.bridge;


interface IMainTeeECallBridge {
    String complexFilter(String data);

    String AuthMainTee(String data);
}

interface IMainTeeOCallBridge {

}

public class MainTeeBridge implements IMainTeeECallBridge, IMainTeeOCallBridge {
    @Override
    public String complexFilter(String data) {
        return null;
    }

    @Override
    public String AuthMainTee(String data) {
        return null;
    }
}
