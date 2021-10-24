package com.example.gav_fx.core;

import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;

import java.util.Set;
import java.util.concurrent.BrokenBarrierException;

public class AlgorithmExecutor implements Runnable {
    
    Set<Node> nodes;
    Algorithm algorithm;
    
    String name;
    OutputType outputType = OutputType.ALGO_EXECUTOR;
    
    // this is a workaround to the problem of naming threads
    // in ExecutorService
    // renames thread once, then does nothing
    Runnable threadNamer = () -> { Thread.currentThread().setName(name); threadNamer = () -> { };};
    
    public AlgorithmExecutor(Set<Node> nodes, Algorithm algorithm, String threadName) {
        this.nodes = nodes;
        this.algorithm = algorithm;
        this.name = threadName;
        System.out.println("new executor named: " + threadName + " tasks=" + nodes);
    }
    
    @Override
    public void run() {
        threadNamer.run();
//        System.out.println("Thread '"+name+"' stared.");
        
        if (algorithm == null) {
            LOG.out(" -> ", "Algorithm == null, returning.", outputType);
        }
        else {
            nodes.forEach(n -> {
                //LOG.out("  ->", "Algo starting on node " + n + ".");
                State newState = algorithm.run(new Vertex(n));
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
        
        try { AlgorithmController.BARRIER.await(); }
        catch (InterruptedException | BrokenBarrierException e) { e.printStackTrace(); }
        
//        LOG.out("  ->", "Barrier tipped, thread exiting.");
    }
    
    public String stateToString() {
        return name + " -> " + nodes.size();
    }
    
    public synchronized void addNewNodeToProcess(Node n) { nodes.add(n); }
    
    public void setAlgorithm(Algorithm algo) { algorithm = algo; }
}
