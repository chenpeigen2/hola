package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.config.model.Host;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class JsonUtils {

    private JsonUtils() {

    }

    @Getter
    private final static Gson gson;

    private static volatile List<Host> hosts;

    static {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @SneakyThrows
    public synchronized static List<Host> readProps() {
        if (hosts == null) {
            hosts = gson.fromJson(new FileReader(Objects.requireNonNull(JsonUtils.class.getClassLoader().getResource("localsid.json")).getFile()), new TypeToken<List<Host>>() {
            }.getType());
        }
        return hosts;
    }

    public static List<String> sids() {
        var hosts = readProps();
        List<String> ans = new ArrayList<>();
        for (var h : hosts) {
            ans.add(h.getSid());
        }
        return ans;
    }

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 5984;

    @Deprecated
    public static String getHost(String sid) {
        var hosts = readProps();
        for (var h : hosts) {
            if (sid.equals(h.getSid())) {
                return h.getHost();
            }
        }
        return DEFAULT_HOST;
    }

    @Deprecated
    public static int getPort(String sid) {
        var hosts = readProps();
        for (var h : hosts) {
            if (sid.equals(h.getSid())) {
                return h.getPort();
            }
        }
        return DEFAULT_PORT;
    }

    public static Host host(String sid) {
        var hosts = readProps();
        for (var h : hosts) {
            if (sid.equals(h.getSid())) {
                return h;
            }
        }
        return null;
    }
}
