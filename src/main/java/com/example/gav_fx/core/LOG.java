package com.example.gav_fx.core;

public class LOG {
    
    public static void out(String premsg, String msg) {
        System.out.println(premsg + "["+Thread.currentThread().getName()+"]: " + msg);
    }
}
