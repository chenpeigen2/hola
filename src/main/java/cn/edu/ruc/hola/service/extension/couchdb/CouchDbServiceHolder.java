package cn.edu.ruc.hola.service.extension.couchdb;

import cn.edu.ruc.hola.config.model.Host;
import cn.edu.ruc.hola.utils.JsonUtils;

/**
 * class mapping sid to the spec service
 */
public final class CouchDbServiceHolder {

    @Deprecated
    public static String host(String sid) {
        return JsonUtils.getHost(sid);
    }

    @Deprecated
    public static int port(String sid) {
        return JsonUtils.getPort(sid);
    }

    public static Host getHost(String sid) {
        return JsonUtils.host(sid);
    }
}
