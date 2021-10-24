package com.example.gav_fx.core;

import com.example.gav_fx.panes.leftpane.tabs.LogTab;


public class LOG {
    public static void out(String premsg, String msg, OutputType outType) {
        String logText = premsg + "["+Thread.currentThread().getName()+"]: " + msg;
        System.out.println(logText);
        LogTab.logText(logText, outType.getColor());
    }
}
