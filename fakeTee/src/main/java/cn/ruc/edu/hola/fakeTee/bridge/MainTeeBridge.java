package cn.ruc.edu.hola.fakeTee.bridge;


interface IMainTeeBridge {
    String complexFilter(String data);

    String AuthTee(String data);
}

public class MainTeeBridge implements IMainTeeBridge {
    @Override
    public String complexFilter(String data) {
        return null;
    }

    @Override
    public String AuthTee(String data) {
        return null;
    }
}
