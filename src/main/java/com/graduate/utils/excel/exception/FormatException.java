package com.graduate.utils.excel.exception;

/**
 * Created by konglinghai on 2017/4/30.
 * excel格式异常
 */
public class FormatException extends Exception {
    public FormatException(String message) {
        super(message);
    }

    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
