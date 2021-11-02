package com.example.gav_fx.core;

import java.util.List;

public class NodeState {
    
    public int info;
    
    public NodeState(int info) {
        this.info = info;
    }
    
    public NodeState() {
        this(0);
    }
    
    public Integer getState() {
        return info;
    }
    
    public void setState(int info) {
        this.info = info;
    }
    
    // for debug component drawer, mark current state State with "|"
    public static String stateListToString(List<NodeState> list) {
        StringBuilder sb = new StringBuilder()
                .append("[");
        
        // this could be optimized by spliting for loops into two
        // one from 0...curretState
        // append |currstate|
        // continue from currState...list.len
        for (int i=0; i<list.size()-1; i++) {
            int state = list.get(i).getState();
            sb.append(WorkerController.currentStateIndex == i ? "|"+state+"|, " : state+", ");
        }
        int state = list.get(list.size()-1).getState();
        sb.append(WorkerController.currentStateIndex == list.size()-1 ? "|"+state+"|" : state);
        return sb.append("]").toString();
    }
    
    @Override
    public String toString() {
        return info + "";
    }
}
