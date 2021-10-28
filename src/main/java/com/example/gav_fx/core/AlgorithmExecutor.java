package com.example.gav_fx.core;

import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import com.example.gav_fx.panes.bottompane.tabs.PerformanceTab;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicReference;

public class AlgorithmExecutor extends Thread {
    
    Set<Node> nodesToProcess;
    Algorithm algorithm;
    
    public String name;
    OutputType outputType = OutputType.ALGO_EXECUTOR;
    
    private static final ThreadMXBean THREAD_MX_BEAN = ManagementFactory.getThreadMXBean();
    private ThreadCPUTimeObservable info;
    
    public AlgorithmExecutor(Set<Node> nodesToProcess, Algorithm algorithm, String threadName) {
        this.nodesToProcess = nodesToProcess;
        this.algorithm = algorithm;
        this.name = threadName;
        System.out.println("new executor named: " + threadName + " tasks=" + nodesToProcess);
    }
    
    @Override
    public void run() {
        this.setName(name);
        info = new ThreadCPUTimeObservable(name + ": -1");
        PerformanceTab.bindThreadInfo(name, info);
        
        LOG.out("===>", "Thread '"+name+"' stared.", outputType);
        
        while (true) {
            try {AlgorithmController.BARRIER.await(); }
            catch (InterruptedException | BrokenBarrierException e) { e.printStackTrace(); }
            
            if (algorithm == null) {
                LOG.out(" -> ", "Algorithm == null, returning.", outputType);
            } else {
                nodesToProcess.forEach(n -> {
                    //LOG.out("  ->", "Algo starting on node " + n + ".");
                    com.example.gav_fx.core.State newState = algorithm.run(new Vertex(n));
                    n.addState(newState);
            
                    if (newState.getState() >= 1) n.setFill(Node.INFORMED_COLOR);
            
                    if (newState.getState() >= 1 &&
                            n.states.get(AlgorithmController.currentStateIndex).getState() == 0) {
                        MyGraph.getInstance().signalNewInformedNode();
                    }
                    //LOG.out("  ->", "Algo done on node     " + n + ".");
                });
                LOG.out("  ->", "AlgoExecutor done for all nodes, waiting on barrier.", outputType);
            }
            info.computeValue().updateValue(name + ": " + THREAD_MX_BEAN.getThreadCpuTime(Thread.currentThread().getId()));
        }
        
        //LOG.out("===>", "Thread " + name + " shutting down.", outputType);
    }
    
    public String stateToString() { return name + " -> " + nodesToProcess.size(); }
    
    public synchronized void addNewNodeToProcess(Node n) { nodesToProcess.add(n); }
    
    public void setAlgorithm(Algorithm algo) { algorithm = algo; }
    
    
    /**
     * Not sure if this is working correctly... suspicious
     */
    
    public static class ExecutorThreadInfo {
        final StringProperty threadCPUTime;
        
        public ExecutorThreadInfo(String initialValue) {
            threadCPUTime = new SimpleStringProperty();
            threadCPUTime.setValue(initialValue);
        }
        
        public StringProperty property() { return this.threadCPUTime; }
        
        public void updateValue(String newValue) {
            Platform.runLater(() -> threadCPUTime.setValue(newValue));
        }
    }
    
    public static class ThreadCPUTimeObservable extends ObjectBinding<ExecutorThreadInfo> {
        private final ExecutorThreadInfo value;
        
        public ThreadCPUTimeObservable(String initialValue) {
            this.value = new ExecutorThreadInfo(initialValue);
            bind(this.value.property());
        }
        
        @Override
        protected ExecutorThreadInfo computeValue() {
            return value;
        }
    }
}
