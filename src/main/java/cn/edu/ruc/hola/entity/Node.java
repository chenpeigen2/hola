package cn.edu.ruc.hola.entity;

/**
 * query condition-value holder
 *
 * @param condition : condition value the user pass
 * @param value     : with condition
 */
public record Node(String condition, String value) {
}
