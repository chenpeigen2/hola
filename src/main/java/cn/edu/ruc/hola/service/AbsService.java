package cn.edu.ruc.hola.service;

import lombok.Getter;
import lombok.Setter;

public abstract class AbsService implements Service {

    /**
     * service id that represents the service
     */
    @Getter
    @Setter
    private String sid;

    public AbsService(String sid) {
        this.sid = sid;
    }
}
