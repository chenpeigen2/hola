package cn.ruc.edu.hola.backbend.entity.inner;

import com.google.common.base.Objects;

import java.util.Set;

public class $path {
    private String path;

    private Set<String> keyWords;

    public $path(String path, Set<String> keyWords) {
        this.path = path;
        this.keyWords = keyWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        $path $path = ($path) o;
        return Objects.equal(path, $path.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }
}
