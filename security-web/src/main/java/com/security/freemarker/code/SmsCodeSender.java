package com.security.freemarker.code;

public interface SmsCodeSender {

    void send(String mobile, String code);
}
