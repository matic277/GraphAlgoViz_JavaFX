package com.example.gav_fx.core;

import com.example.gav_fx.graph.Node;

import java.util.List;

public class Vertex {
    
    Node node;
    
    // Wrapper class for Node
    // This is what is exposed to the user on Algorithm implementation
    public Vertex(Node node) {
        this.node = node;
    }
    
    public List<Node> getNeighbors() {
        return node.getNeighbors();
    }
    
    public NodeState getState() {
        return node.getState();
    }
}
