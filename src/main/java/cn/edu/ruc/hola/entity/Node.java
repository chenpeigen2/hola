package cn.edu.ruc.hola.entity;

import cn.edu.ruc.hola.utils.SHA256Utils;

/**
 * query condition-value holder
 *
 * @param condition : condition value the user pass
 * @param value     : with condition
 */
public record Node(String condition, String value) {
    public String valueSHA() {
        return SHA256Utils.getSHA256Str(value);
    }

    public String conditionSHA() {
        return SHA256Utils.getSHA256Str(condition);
    }

    public String dataSHA() {
        return SHA256Utils.getSHA256Str(toString());
    }
}
