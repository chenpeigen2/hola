package cn.ruc.edu.hola.client.entity;

import cn.ruc.edu.hola.client.utils.RandomStringGenerator;

public class AuthRequest extends Request {
    public AuthRequest(String query, String db_name, String filter) {
        super(query, db_name, filter);
        this.query += RandomStringGenerator.generateRandomString(24);
        this.db_name += RandomStringGenerator.generateRandomString(24);
        this.filter += RandomStringGenerator.generateRandomString(24);
    }
}
