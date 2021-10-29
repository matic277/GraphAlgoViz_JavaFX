package com.example.gav_fx.core;

import com.example.gav_fx.components.leftpane.tabs.LogTab;


public class LOG {
    public static void out(String premsg, String msg, OutputType outType) {
        String logText = premsg + "["+Thread.currentThread().getName()+"]: " + msg;
        System.out.println(logText);
        LogTab.logText(logText, outType.getColor());
    }
    
    public static void out(String msg) {
        String logText = "["+Thread.currentThread().getName()+"]: " + msg;
        System.out.println(logText);
        LogTab.logText(logText, OutputType.DEBUG.getColor());
    }
    
    public static void error(String msg) {
        String logText = "["+Thread.currentThread().getName()+"]: " + msg;
        System.out.println(logText);
        LogTab.logText(logText, OutputType.ERROR.getColor());
    }
    
    public static void warning(String msg) {
        String logText = "["+Thread.currentThread().getName()+"]: " + msg;
        System.out.println(logText);
        LogTab.logText(logText, OutputType.WARNING.getColor());
    }
}
