package com.example.gav_fx;

import com.example.gav_fx.core.*;
import com.example.gav_fx.graph.Node;

public class AlgoImpl1 implements AlgorithmImplementor {
    
    public AlgoImpl1() {}
    
    @Override
    public Algorithm getAlgorithm() {
        return vertex -> {
            // if you have info, don't do anything
            if (vertex.getState().info > 0) return new NodeState(vertex.getState().info);
            
            // Some nodes have no neighbors, so
            // in this case don't do anything.
            // Return the same state you're in.
            if (vertex.getNeighbors().isEmpty()) return new NodeState(vertex.getState().info);
            
            // get two random neighbors
            Node randNeigh1 = vertex.getNeighbors().get(Tools.RAND.nextInt(vertex.getNeighbors().size()));
            Node randNeigh2 = vertex.getNeighbors().get(Tools.RAND.nextInt(vertex.getNeighbors().size()));
            NodeState nodeStateOfNeigh1 = randNeigh1.getState();
            NodeState nodeStateOfNeigh2 = randNeigh2.getState();
            
            // or
            int newStateInfo = nodeStateOfNeigh1.info | nodeStateOfNeigh2.info | vertex.getState().info;
            
            return new NodeState(newStateInfo);
        };
    }
}
