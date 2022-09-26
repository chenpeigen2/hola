package cn.edu.ruc.hola.web;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * indicate
 */
@Retention(RetentionPolicy.SOURCE)
public @interface UseSpringBoot {
    boolean flag() default true;
}
