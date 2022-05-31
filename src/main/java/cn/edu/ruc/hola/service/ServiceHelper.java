package cn.edu.ruc.hola.service;

import lombok.NonNull;

/**
 * class for access the service
 */
public class ServiceHelper {

    /**
     * create a service that a man can hold
     */
    public static Service createService(String sid, @NonNull ServiceType type) {
        switch (type) {
            case TYPE_COUCHDB -> {
                return new CouchService(sid);
            }
            case TYPE_MYSQL -> System.out.println("TYPE_MYSQL");
            default -> System.out.println("default");
        }
        return null;
    }
}
