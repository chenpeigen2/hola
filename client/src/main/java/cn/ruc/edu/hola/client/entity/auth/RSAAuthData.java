package cn.ruc.edu.hola.client.entity.auth;

import cn.ruc.edu.hola.client.encrypy.EncryptUtils;

public class RSAAuthData extends AuthData {
    public RSAAuthData(String uid, String key) {
        super(uid, key);
        this.key = EncryptUtils.rsaEncrypt(this.key, EncryptUtils.RSA_PUBLIC_KEY);
    }
}
