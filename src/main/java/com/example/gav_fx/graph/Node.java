package com.example.gav_fx.graph;

import com.example.gav_fx.App;
import com.example.gav_fx.core.AlgorithmController;
import com.example.gav_fx.core.State;
import com.example.gav_fx.panes.GraphPane;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import org.jgrapht.Graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jgrapht.alg.drawing.model.Point2D;

public class Node extends Circle {
    
    public int id;
    
    public int info = 0;
    //    public Set<Node> neighbors;
    public List<State> states;
    
    private static Color BORDER_COLOR = Color.BLACK; // default
    
    public static Color INFORMED_COLOR = Color.GREEN;
    public static Color UNINFORMED_COLOR = Color.BLACK;
    
    public static double BORDER_WIDTH = 3;
    public static double NODE_RADIUS = 30;
    private static final double ON_DRAG_ENLARGEMENT_PERCENTAGE = 1.1; // on drag, enlarge TODO this shoud be scaled based on NODE_RADIUS (bigger circles get enlarged more, smaller less, not ideal! (maybe keep 1.1, but do min/max if perc enlargement is too low/high))
    private static final Border ON_HOVER_BORDER = new Border(new BorderStroke(Color.WHITESMOKE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
    
    final Delta dragDelta = new Delta();
    static class Delta { double x, y; }
    
    // Edge creating
    public static final AtomicReference<Node> clickedNodeRef = new AtomicReference<>(null);
    public static final AtomicReference<Line> edgeRef = new AtomicReference<>(null);
    
    private Label coordsInfo;
    private boolean coordsShowing = false;
    private Label neighboursInfo;
    private boolean neighboursShowing = false;
    
    @Deprecated(since = "Do not use this constructor, use MyGraph.newNode()")
    public Node(int x, int y, int id) {
        super(x, y, 30);
        this.id = id;
        
        this.setRadius(NODE_RADIUS);
        this.setFill(UNINFORMED_COLOR);
        
        states = new ArrayList<>(10);
        states.add(new State(0)); // uninformed on initialize
        
        // border
        setStroke(BORDER_COLOR);
        setStrokeType(StrokeType.OUTSIDE);
        setStrokeWidth(BORDER_WIDTH);
        
        // TODO implementing draggable nodes:
        //  https://stackoverflow.com/questions/49951275/binding-line-with-circles-coordinate-doesnt-work
        this.setOnMouseDragged(event -> {
            GraphPane.INSTANCE.getChildren().remove(edgeRef.get());
            clickedNodeRef.set(null);
            edgeRef.set(null);
            
            setCenterX(event.getX() + dragDelta.x);
            setCenterY(event.getY() + dragDelta.y);
            
            if (coordsInfo != null) {
                updateCoordsInfo();
            }
            
            event.consume();
        });
        this.setOnMouseClicked(event -> {
        
        });
    
        // Right-click menu for deleting edges
        this.setOnContextMenuRequested(e -> GraphPane.INSTANCE.openContextMenuForNode(e));
        
        this.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) return;
            
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
                edge.setStrokeWidth(Edge.strokeWidth);
                edge.setStartX(this.getCenterX());
                edge.setStartY(this.getCenterY());
                
                // TODO:
                //   maybe this can be achieved with some translate to parent local method or something
                //   don't know enough about those methods atm
                //   these static fields aren't the prettiest but they seem to work
                //   not anymore (did they ever)??
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
        
        // Highlight on mouse hover
        this.setOnMouseEntered(e -> {
            setNewBorderColor(BORDER_COLOR.invert());
        });
        this.setOnMouseExited(e -> {
            setNewBorderColor(BORDER_COLOR.invert());
        });
    }
    
    public void showCoordsInfo() {
        coordsShowing = true;
        coordsInfo = new Label();
        updateCoordsInfo();
        
        coordsInfo.layoutXProperty().bind(centerXProperty().add(radiusProperty()));
        coordsInfo.layoutYProperty().bind(centerYProperty().add(radiusProperty()));
        GraphPane.INSTANCE.addNodeLabel(coordsInfo);
    }
    public void hideCoordsInfo() {
        coordsShowing = false;
        if (coordsInfo == null) return; // shouldn't really happen
        GraphPane.INSTANCE.removeNodeLabel(coordsInfo);
    }
    public void updateCoordsInfo() { coordsInfo.setText("(" + (int)getCenterX() + ", " + (int)getCenterY() + ")"); }
    public boolean areCoordsShowing() { return coordsShowing; }
    
    public void drawNeighboursInfo() {
        neighboursShowing = true;
        neighboursInfo = new Label();
        updateNeighboursInfo();
        
        neighboursInfo.layoutXProperty().bind(centerXProperty().add(radiusProperty()));
        neighboursInfo.layoutYProperty().bind(centerYProperty().add(radiusProperty()).add(20));
        GraphPane.INSTANCE.addNodeLabel(neighboursInfo);
    }
    public void hideNeighboursInfo() {
        neighboursShowing = true;
        if (neighboursInfo == null) return; // shouldn't really happen
        GraphPane.INSTANCE.removeNodeLabel(neighboursInfo);
    }
    public void updateNeighboursInfo() {
        if (!neighboursShowing) return;
        neighboursInfo.setText(getNeighbors().stream().map(n -> n.getNodeId()+"").collect(Collectors.joining(", ", "{", "}")));
    }
    public boolean areNeighboursShowing() { return neighboursShowing; }
    
    // Circle.setRadius() method is for some reason final ???
    public void setNewRadius(double newRadius) {
        NODE_RADIUS = newRadius;
        super.setRadius(NODE_RADIUS);
    }
    
    public void setNewBorderColor(Color newColor) {
        BORDER_COLOR = newColor;
        this.setStroke(BORDER_COLOR); // does this variable even need to exist?
    }
    
    public void setNewBorderWidth(double newWidth) {
        BORDER_WIDTH = newWidth;
        this.setStrokeWidth(BORDER_WIDTH);
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
