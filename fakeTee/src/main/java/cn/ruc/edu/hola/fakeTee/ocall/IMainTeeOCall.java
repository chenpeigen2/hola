package cn.ruc.edu.hola.fakeTee.ocall;


public interface IMainTeeOCall {
    void print(String... text);

    static IMainTeeOCall getInstance() {
        return new MainTeeOCall();
    }
}

class MainTeeOCall implements IMainTeeOCall {
    @Override
    public void print(String... text) {

    }
}
