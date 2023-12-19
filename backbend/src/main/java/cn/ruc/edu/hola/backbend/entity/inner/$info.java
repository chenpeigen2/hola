package cn.ruc.edu.hola.backbend.entity.inner;

import com.google.common.base.Objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class $info {
    private String $name;

    private String $url;

    private List<$path> $path;


    public $info(String $name, String $url, List<cn.ruc.edu.hola.backbend.entity.inner.$path> $path) {
        this.$name = $name;
        this.$url = $url;
        this.$path = $path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        $info $info = ($info) o;
        return Objects.equal($name, $info.$name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode($name);
    }

    public static void main(String[] args) {
        Map<$info, Integer> map = new HashMap<>();
        map.put(new $info("abc", "sdf", null), 1);

        System.out.println(map.containsKey(new $info("abc", null, null)));
    }
}


