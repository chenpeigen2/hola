package cn.ruc.edu.hola.backbend.okhttp;

import cn.ruc.edu.hola.backbend.config.Options;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class CookieJarImpl implements CookieJar {

    private static final boolean DEBUG = Options.DEBUG;

    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) {
            throw new IllegalArgumentException("cookieStore can not be null.");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
        if (DEBUG) {
            System.out.println("save in url:" + url);
            cookies.forEach(System.out::println);
        }
        this.cookieStore.add(url, cookies);
    }

    @NotNull
    @Override
    public synchronized List<Cookie> loadForRequest(@NotNull HttpUrl url) {
        if (DEBUG) {
            System.out.println("load in url:" + url);
            this.cookieStore.get(url).forEach(cookie -> System.out.println(cookie.toString()));
        }
        return this.cookieStore.get(url);
    }

    public CookieStore getCookieStore() {
        return this.cookieStore;
    }
}