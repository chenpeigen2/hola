package cn.edu.ruc.hola.fake;

import com.google.gson.Gson;

public class Social {
    private String uid;

    private String nickName; // the name

    private int gender; // 1 : boy ; 0 : girl

    private String social_id; // class id

    public Social(String uid, String nickName, int gender, String social_id) {
        this.uid = uid;
        this.nickName = nickName;
        this.gender = gender;
        this.social_id = social_id;
    }

    public static void testSeizeJson() {
        Social social = new Social("aa", "bb", 1, "dd");
        System.out.println(new Gson().toJson(social));
    }

    public static void main(String[] args) {
        testSeizeJson();
    }
}
