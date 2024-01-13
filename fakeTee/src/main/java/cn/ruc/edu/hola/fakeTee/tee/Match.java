package cn.ruc.edu.hola.fakeTee.tee;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.*;

public final class Match {

    private static final String IndexUrl = "http://localhost:3000/aggregate_com";

    private static final Gson gson = new Gson();

    private static final Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
    }

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)).build();


    public static List<String> simpleMatch(String body) {
        RequestBody requestBody = RequestBody.create(body, MediaType.get("application/json"));
        var request = new okhttp3.Request.Builder()
                .url(IndexUrl)
                .method("POST", requestBody)
                .headers(Headers.of(headers))
                .tag("simpleMatch#TEE")
                .build();

        List<String> ret = new ArrayList<>();

        try (var response = client.newCall(request).execute()) {
            var postData = response.body().string();
            // parse the json data to the array
            ret = gson.fromJson(postData, new TypeToken<>() {
            });
        } catch (Exception e) {
            System.err.println(e);
        }

        return ret;
    }

    public static void main(String[] args) {
        var data = "[\"social_id\",\"gender\"]";
        var urlList = simpleMatch(data);

        for (int k = 0; k < urlList.size(); ) {
            if (urlList.get(k).equals("$url")) {
                k++; // move to $url value
                String bbUrl = urlList.get(k);
                k++; // move to $find
                if (urlList.get(k).equals("$find")) {
                    k++; // move to $find value
                    String bbFind = urlList.get(k);
                    System.out.println(bbUrl);
                    System.out.println(bbFind);
                }
                k++;
            }
        }

        System.out.println(urlList);
    }
}
