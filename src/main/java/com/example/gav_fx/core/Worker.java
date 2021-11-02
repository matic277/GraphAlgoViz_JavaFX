package com.example.gav_fx.core;

import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.BrokenBarrierException;

public class Worker extends Thread {
    
    public static final Object WORKER_LOCK = new Object();
    
    Algorithm algorithm;
    
    public String name;
    OutputType outputType = OutputType.ALGO_EXECUTOR;
    
    private static final ThreadMXBean THREAD_MX_BEAN = ManagementFactory.getThreadMXBean();
    //private ThreadCPUTimeObservable info;
    
    public Worker(Algorithm algorithm, String threadName) {
        this.algorithm = algorithm;
        this.name = threadName;
        LOG.out(" + ", "New executor named: " + threadName, outputType);
    }
    
    @Override
    public void run() {
        this.setName(name);
        LOG.out(" + ", "Thread '"+name+"' stared.", outputType);
        
        while (!WorkerController.STOP_THREAD.get()) {
            // Wait for controller to do some work management
            // and wake you up
            synchronized (WORKER_LOCK) {
                try { WORKER_LOCK.wait(); }
                catch (InterruptedException e) { e.printStackTrace(); }
            }
            
            // Management by controller is done. Processing can continue
            LOG.warning("Worker waken up");
            processWork();
            
            // Signal that your work is done... waiting for others
            try { WorkerController.BARRIER.await(); }
            catch (InterruptedException | BrokenBarrierException e) { e.printStackTrace(); }
            LOG.warning("Barrier broken");
        }
        
        LOG.out(" - ", "Thread " + name + " shutting down.", outputType);
    }
    
    public void processWork() {
        LOG.out("Processing batches.", outputType);
        if (algorithm == null) {
            LOG.out(" -> ", "Algorithm == null, returning.", outputType);
            //WorkerController.PROCESSED_BATCHES.add(WorkerController.WORK_BATCHES.pop());
            return;
        }
        
        int processedBatches = 0;
        WorkBatch workBatch;
        while((workBatch = WorkerController.WORK_BATCHES.poll()) != null) {
            // there is still work to do, so process it
            workBatch.getNodesToProcess().forEach(this::processNode);
            WorkerController.PROCESSED_BATCHES.add(workBatch);
            processedBatches++;
        }
        LOG.out("Done processing, processed  " + processedBatches + " batches.", outputType);
    }
    
    private void processNode(Node n) {
        //LOG.out("Algo starting on node " + n + " with currIndex " + WorkerController.currentStateIndex);
        NodeState newState = algorithm.run(new Vertex(n));
        n.addState(newState);
        
        if (newState.getState() >= 1) n.setFill(Node.INFORMED_COLOR);
        
        if (newState.getState() >= 1 &&
                n.states.get(WorkerController.currentStateIndex).getState() == 0) {
            MyGraph.getInstance().signalNewInformedNode();
        }
        //LOG.out("  ->", "Algo done on node     " + n + ".");
    }
    
    
    //public String stateToString() { return name + " -> " + nodesToProcess.size(); }
    
    public void setAlgorithm(Algorithm algo) { algorithm = algo; }
    
    
    /**
     * Not sure if this is working correctly... suspicious
     */
    //public static class ExecutorThreadInfo {
    //    final StringProperty threadCPUTime;
    //
    //    public ExecutorThreadInfo(String initialValue) {
    //        threadCPUTime = new SimpleStringProperty();
    //        threadCPUTime.setValue(initialValue);
    //    }
    //
    //    public StringProperty property() { return this.threadCPUTime; }
    //
    //    public void updateValue(String newValue) {
    //        LOG.out("->", "Updated value " + newValue, OutputType.WARNING);
    //        Platform.runLater(() -> {
    //            threadCPUTime.setValue(newValue);
    //        });
    //    }
    //}
    //
    //public static class ThreadCPUTimeObservable extends ObjectBinding<ExecutorThreadInfo> {
    //    private final ExecutorThreadInfo value;
    //
    //    public ThreadCPUTimeObservable(String initialValue) {
    //        this.value = new ExecutorThreadInfo(initialValue);
    //        bind(this.value.property());
    //    }
    //
    //    @Override
    //    protected ExecutorThreadInfo computeValue() {
    //        return value;
    //    }
    //}
}
