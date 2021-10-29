package com.example.gav_fx.core;

import com.example.gav_fx.graph.Node;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// Represents a batch of work.
// WorkBatch is a "small" collection of nodes that must be processed by a single Worker.
// Meaning, Worker executes Algorithm on each Node.
public class WorkBatch {
    
    private Set<Node> workBatch;
    
    public WorkBatch(Set<Node> workBatch) {
        this.workBatch = workBatch;
        //this.workBatch.forEach(n -> n.setBatchParent(this));
    }
    
    public WorkBatch() {
        this.workBatch = new HashSet<>((int)(WorkerController.BATCH_SIZE * 1.1));
        //this.workBatch.forEach(n -> n.setBatchParent(this));
    }
    
    public void addMoreWork(Collection<Node> moreWork) {
        workBatch.addAll(moreWork);
    }
    
    public void addMoreWork(Node moreWork) {
        workBatch.add(moreWork);
    }
    
    public void setWorkBatch(Set<Node> workBatch) {
        this.workBatch = workBatch;
    }
    
    public Set<Node> getNodesToProcess() {
        return workBatch;
    }
}
