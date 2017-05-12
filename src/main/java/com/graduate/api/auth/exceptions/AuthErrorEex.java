package com.graduate.api.auth.exceptions;

/**
 * Created by konglinghai on 2017/4/7.
 */
public class AuthErrorEex extends Exception {
    public AuthErrorEex(String msg) {
        super(msg);
    }
    public AuthErrorEex(String msg, Throwable t) {
        super(msg, t);
    }
}
