package com.example.gav_fx.graph;

import com.example.gav_fx.App;
import com.example.gav_fx.core.AlgorithmController;
import com.example.gav_fx.core.State;
import com.example.gav_fx.panes.GraphPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import org.jgrapht.Graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jgrapht.alg.drawing.model.Point2D;

public class Node extends Circle {
    
    public int id;
    
    public int info = 0;
//    public Set<Node> neighbors;
    public List<State> states;
    
    private static Color borderColor = Color.BLACK; // default
    
    public static Color INFORMED_COLOR = Color.GREEN;
    public static Color UNINFORMED_COLOR = Color.BLACK;
    
    public static double NODE_RADIUS = 30;
    private static double ON_DRAG_ENLARGEMENT_PERCENTAGE = 1.1; // on drag, enlarge TODO this shoud be scaled based on NODE_RADIUS (bigger circles get enlarged more, smaller less, not ideal! (maybe keep 1.1, but do min/max if perc enlargement is too low/high))
    
    final Delta dragDelta = new Delta();
    static class Delta {
        double x, y;
    }
    
    // Edge creating
    public static final AtomicReference<Node> clickedNodeRef = new AtomicReference<>(null);
    public static final AtomicReference<Line> edgeRef = new AtomicReference<>(null);
    
    @Deprecated(since = "Do not use this constructor, use MyGraph.newNode()")
    public Node(int x, int y, int id) {
        super(x, y, 30);
        this.id = id;
        
        this.setFill(UNINFORMED_COLOR);
        
        states = new ArrayList<>(10);
        states.add(new State(0)); // uninformed on initialize
        
        // Border
        //setStroke(Color.BLACK);
        //setStrokeType(StrokeType.OUTSIDE);
        //setStrokeWidth(0); // hide border
        
        // Border
        setStroke(borderColor);
        setStrokeType(StrokeType.OUTSIDE);
        setStrokeWidth(0); // hide border
        
        // TODO implementing draggable nodes:
        //  https://stackoverflow.com/questions/49951275/binding-line-with-circles-coordinate-doesnt-work
        this.setOnMouseDragged(event -> {
            GraphPane.INSTANCE.getChildren().remove(edgeRef.get());
            clickedNodeRef.set(null);
            edgeRef.set(null);
            
            setCenterX(event.getX() + dragDelta.x);
            setCenterY(event.getY() + dragDelta.y);
            
            event.consume();
        });
        this.setOnMouseClicked(event -> {
        
        });
        
        this.setOnMousePressed(event -> {
            // creating edge
            if (clickedNodeRef.get() != null) {
                if (this != clickedNodeRef.get()) {
                    MyGraph.getInstance().addEdge(this, clickedNodeRef.get());
                }
                GraphPane.INSTANCE.getChildren().remove(edgeRef.get());
                clickedNodeRef.set(null);
                edgeRef.set(null);
            }
            else {
                GraphPane.INSTANCE.getChildren().remove(edgeRef.get());
                clickedNodeRef.set(this);
                Line edge = new Line();
                edge.setStartX(this.getCenterX());
                edge.setStartY(this.getCenterY());
    
                // TODO:
                //   maybe this can be achieved with some translate to parent local method or something
                //   don't know enough about those methods atm
                //   these static fields aren't the prettiest but they seem to work
                edge.endXProperty().bind(App.MOUSE_LOCATION.x.subtract(GraphPane.OFFSET_X).subtract(App.LEFT_MENU_WIDTH));
                edge.endYProperty().bind(App.MOUSE_LOCATION.y.subtract(GraphPane.OFFSET_Y));
                edgeRef.set(edge);
                GraphPane.INSTANCE.getChildren().add(edge);
            }
            
            // dragging
            dragDelta.x = getCenterX() - event.getX();
            dragDelta.y = getCenterY() - event.getY();
            
            toFront();
            setRadius(NODE_RADIUS * ON_DRAG_ENLARGEMENT_PERCENTAGE);
            event.consume();
        });
        this.setOnMouseReleased(event -> {
            setRadius(NODE_RADIUS);
            event.consume();
        });
    }
    
    // Circle.setRadius() method is for some reason final ???
    public void setNewRadius(double newRadius) {
        super.setRadius(newRadius);
        NODE_RADIUS = newRadius;
    }
    
    public void setNewBorderColor(Color newColor) {
        borderColor = newColor;
        this.setStroke(borderColor); // does this variable even need to exist?
    }
    
    public void setNewBorderWidth(double newWidth) {
        this.setStrokeWidth(newWidth);
    }
    
    public int getNodeId() { return this.id; }
    
    public State getState() { return this.states.get(AlgorithmController.currentStateIndex); }
    
    public void addState(State state) {
        this.states.add(state);
    }
    
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
