package cn.ruc.edu.hola.fakeTee.ocall;


public interface IMainTeeOCall {
    void oCallPrint(String... text);

    static IMainTeeOCall getInstance() {
        return new MainTeeOCall();
    }
}

class MainTeeOCall implements IMainTeeOCall {
    @Override
    public void oCallPrint(String... text) {

    }
}
