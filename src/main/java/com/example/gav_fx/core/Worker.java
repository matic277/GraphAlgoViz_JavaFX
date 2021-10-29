package com.example.gav_fx.core;

import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import com.example.gav_fx.panes.bottompane.tabs.PerformanceTab;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.BrokenBarrierException;

public class Worker extends Thread {
    
    Algorithm algorithm;
    
    public String name;
    OutputType outputType = OutputType.ALGO_EXECUTOR;
    
    private static final ThreadMXBean THREAD_MX_BEAN = ManagementFactory.getThreadMXBean();
    private ThreadCPUTimeObservable info;
    
    public Worker(Algorithm algorithm, String threadName) {
        this.algorithm = algorithm;
        this.name = threadName;
        LOG.out(" + ", "New executor named: " + threadName, outputType);
    }
    
    @Override
    public void run() {
        this.setName(name);
        info = new ThreadCPUTimeObservable(name + ": -1");
        PerformanceTab.bindThreadInfo(name, info);
        
        LOG.out("===>", "Thread '"+name+"' stared.", outputType);
        
        while (true) {
            try {
                WorkerController.BARRIER.await(); }
            catch (InterruptedException | BrokenBarrierException e) { e.printStackTrace(); }
            
            processWork();
            
            LOG.out("  ->", "No more work, waiting on barrier.", outputType);
            
            // artificial work
            //for (int i=0; i<10000; i++) System.out.println(i);
            
            long threadCpuTimeMiliseconds = THREAD_MX_BEAN.getThreadCpuTime(Thread.currentThread().getId()) / 1_000_000;
            info.computeValue().updateValue(name + ": " + threadCpuTimeMiliseconds);
        }
        
        //LOG.out("===>", "Thread " + name + " shutting down.", outputType);
    }
    
    public void processWork() {
        if (algorithm == null) {
            LOG.out(" -> ", "Algorithm == null, returning.", outputType);
            return;
        }
        
        WorkBatch workBatch;
        while((workBatch = WorkerController.WORK_BATCHES.pop()) != null) {
            // there is still work to do, so process it
            workBatch.getNodesToProcess().forEach(this::processNode);
            WorkerController.PROCESSED_BATCHES.add(workBatch);
        }
    }
    
    private void processNode(Node n) {
        //LOG.out("  ->", "Algo starting on node " + n + ".");
        NodeState newState = algorithm.run(new Vertex(n));
        n.addState(newState);
        
        if (newState.getState() >= 1) n.setFill(Node.INFORMED_COLOR);
        
        if (newState.getState() >= 1 &&
                n.nodeStates.get(WorkerController.currentStateIndex).getState() == 0) {
            MyGraph.getInstance().signalNewInformedNode();
        }
        //LOG.out("  ->", "Algo done on node     " + n + ".");
    }
    
    
    //public String stateToString() { return name + " -> " + nodesToProcess.size(); }
    
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
            LOG.out("->", "Updated value " + newValue, OutputType.WARNING);
            Platform.runLater(() -> {
                threadCPUTime.setValue(newValue);
            });
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
