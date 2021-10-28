package com.example.gav_fx.panes.bottompane.tabs;

import com.example.gav_fx.core.AlgorithmExecutor;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.SystemUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PerformanceTab extends VBox {
    
    private static PerformanceTab INSTANCE;
    
    public static final Map<String, Label> threadInfoMap = new HashMap<>();
    public static final Map<ThreadInfo, ObservableValue<String>> observableInfo = new HashMap<>();
    public static final Map<ThreadInfo, Label> labelsMap = new HashMap<>();
    
    public PerformanceTab() {
        this.setPadding(new Insets(10));
        //JavaSysMon monitor =   new JavaSysMon();
        //String osName =        monitor.osName();
    
        INSTANCE = this;
    
        //for(Long threadID : threadMXBean.getAllThreadIds()) {
        //    ThreadInfo info = threadMXBean.getThreadInfo(threadID);
        //    System.out.println("Thread name: " + info.getThreadName());
        //    System.out.println("Thread State: " + info.getThreadState());
        //    System.out.println(String.format("CPU time: %s ns",
        //            threadMXBean.getThreadCpuTime(threadID)));
        //}
        
    
        Label cpu1Info = new Label("CPU");
        this.getChildren().add(cpu1Info);
    }
    
    public static void bindThreadInfo(String threadName, AlgorithmExecutor.ThreadCPUTimeObservable info) {
        System.out.println("attempt: " + threadName);
        if (threadInfoMap.get(threadName) != null) return;
        
        System.out.println("yep1");
        
        Label infoLbl = new Label();
        threadInfoMap.put(threadName, infoLbl);
        
        infoLbl.textProperty().bind(info.getValue().property());
        Platform.runLater(() -> INSTANCE.getChildren().add(infoLbl));
    }
}
