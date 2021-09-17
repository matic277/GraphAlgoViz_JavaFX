package com.example.gav_fx.core;

import com.example.gav_fx.graph.Node;

import java.util.List;

public class Vertex {
    
    Node node;
    
    // Wrapper class for Node
    public Vertex(Node node) {
        this.node = node;
    }
    
    public List<Node> getNeighbors() {
        return node.getNeighbors();
    }
    
    public State getState() {
        return node.getState();
    }
}
