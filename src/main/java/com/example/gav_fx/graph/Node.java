package com.example.gav_fx.graph;

import com.example.gav_fx.core.AlgorithmController;
import com.example.gav_fx.core.State;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jgrapht.Graphs;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jgrapht.alg.drawing.model.Point2D;

public class Node extends Circle {
    
    public int id;
    
    public int info = 0;
//    public Set<Node> neighbors;
    public List<State> states;
    

    int messagesReceived = 0;
    int messagesSent = 0;
    
    @Deprecated(since = "Do not use this constructor, use MyGraph.newNode()")
    public Node(int x, int y, int id) {
        super(x, y, 30);
        this.id = id;
        
        this.setFill(Color.AQUA);
    
        states = new ArrayList<>(10);
        states.add(new State(0)); // uninformed on initialize
    }
    
    public int getNodeId() { return this.id; }
    
    public State getState() { return this.states.get(AlgorithmController.currentStateIndex); }
    public void addState(State state) { this.states.add(state); }
    
    public static void setPosition(Node node, Point2D point2D) {
        node.setCenterX(point2D.getX());
        node.setCenterY(point2D.getY());
    }
    
    // TODO how optimal is this?
    public List<Node> getNeighbors() {
        return Graphs.neighborListOf(MyGraph.getInstance().getGraph(), this);
//        return MyGraph.getInstance().getGraph().getEdgeTarget(MyGraph.getInstance().getGraph().edgesOf(this));
    }
    
    
    // equals & hashcode needed due to Node mutation
    // in order to make sure hashset operations work
    
    @Override
    public boolean equals(Object o) {
        if((o == null) || (o.getClass() != this.getClass())) {
            return false;
        }
        return o == this;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.id)
                .toHashCode();
    }
    
    @Override
    public String toString() {
        // do not call getNeighbors() in here!
        // produces StackOverflow when node is deleted
        return "[N="+id+"]{"+"}";
    }
}