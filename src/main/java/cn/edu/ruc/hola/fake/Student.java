package cn.edu.ruc.hola.fake;

import com.google.gson.Gson;
import org.jetbrains.annotations.TestOnly;

import java.util.Date;
import java.util.Random;

public final class Student {
    private String uid; // randomUUID

    private String nickName; // the name

    private String class_id; // class id

    private int gender; // 1 : boy ; 0 : girl

    private Date birthDate; // birthDay

    public Student(String uid, String nickName, String class_id, int gender, Date birthDate) {
        this.uid = uid;
        this.nickName = nickName;
        this.class_id = class_id;
        this.gender = gender;
        this.birthDate = birthDate;
    }


    public static void testSeizeJson() {
        Student student = new Student("aa", "bb", "cc", 1, new Date());
        System.out.println(new Gson().toJson(student));
    }

    @TestOnly
    public static void main(String[] args) {
        testSeizeJson();
    }
}
