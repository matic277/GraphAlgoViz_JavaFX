package com.example.gav_fx.graph;

import com.example.gav_fx.core.*;
import com.example.gav_fx.components.GraphPane;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jgrapht.Graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jgrapht.alg.drawing.model.Point2D;

public class Node extends Circle implements StateObserver {
    
    public int id;
    
    public int info = 0;
    //    public Set<Node> neighbors;
    public List<NodeState> states;
    public WorkBatch workBatch; // tells which batch this node is in, for faster removing on node delete
    
    private Color color;
    
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
    
    private static final int INFO_FONT_SIZE = 12;
    private static final int EXTRA_INFO_VERTICAL_SPACING = 3;
    private static final Font ID_INFO_FONT = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, INFO_FONT_SIZE);
    private static final Font INFO_FONT = Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, INFO_FONT_SIZE);
    private Label idInfo;
    private boolean idShowing = false;
    private Label coordsInfo;
    private boolean coordsShowing = false;
    private Label neighboursInfo;
    private boolean neighboursShowing = false;
    private Label statesInfo;
    private boolean statesShowing = false;
    
    private static Node HIGHLIGHTED_NODE = null; // only one highlighted node at a time for now...
    
    @Deprecated(since = "Do not use this constructor, use MyGraph.newNode()")
    public Node(int x, int y, int id) {
        super(x, y, 30);
        this.id = id;
        
        this.setRadius(NODE_RADIUS);
        this.setFill(UNINFORMED_COLOR);
        
        states = new ArrayList<>(10);
        states.add(new NodeState(0)); // uninformed on initialize
        
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
                //edge.endXProperty().bind(App.MOUSE_LOCATION.x.subtract(GraphPane.OFFSET_X).subtract(App.LEFT_MENU_WIDTH));
                //edge.endYProperty().bind(App.MOUSE_LOCATION.y.subtract(GraphPane.OFFSET_Y));
    
                // TODO
                //  This does not work... node is flashing on hover...
                //  This does not work when mouse goes out of bounds of GraphPane...
                edge.endXProperty().bind(GraphPane.MOUSE_LOCATION.x);
                edge.endYProperty().bind(GraphPane.MOUSE_LOCATION.y);
                
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
    
    public void reflectCurrentStateIndex() {
        NodeState stateToSet = states.get(WorkerController.currentStateIndex);
        this.setNodeColor(stateToSet.getState() >= 1 ?
                INFORMED_COLOR : UNINFORMED_COLOR);
        updateStatesInfo();
    }
    
    public void highlight() {
        if (HIGHLIGHTED_NODE != null) HIGHLIGHTED_NODE.setEffect(null);
        HIGHLIGHTED_NODE = this;
        HIGHLIGHTED_NODE.setEffect(Tools.SHADOW_EFFECT_SEARCH);
    }
    
    public void showIdInfo() {
        if (idShowing) return;
        idShowing = true;
        idInfo = new Label(id + "");
        idInfo.setFont(ID_INFO_FONT);
    
        idInfo.layoutXProperty().bind(centerXProperty().add(radiusProperty().add(10)));
        idInfo.layoutYProperty().bind(centerYProperty().subtract(radiusProperty()));
        GraphPane.INSTANCE.addNodeLabel(idInfo);
    }
    public void hideIdInfo() {
        idShowing = false;
        if (idInfo == null) return; // shouldn't really happen
        GraphPane.INSTANCE.removeNodeLabel(idInfo);
    }
    public boolean isIdShowing() { return idShowing; }
    
    public void showCoordsInfo() {
        if (coordsShowing) return;
        coordsShowing = true;
        coordsInfo = new Label();
        coordsInfo.setFont(INFO_FONT);
        
        updateCoordsInfo(); // Using .bind() actually seems like more work... so just do this instead
        coordsInfo.layoutXProperty().bind(centerXProperty().add(radiusProperty().add(10)));
        coordsInfo.layoutYProperty().bind(centerYProperty().subtract(radiusProperty()).add(EXTRA_INFO_VERTICAL_SPACING + INFO_FONT_SIZE));
        GraphPane.INSTANCE.addNodeLabel(coordsInfo);
    }
    public void hideCoordsInfo() {
        coordsShowing = false;
        if (coordsInfo == null) return; // shouldn't really happen
        GraphPane.INSTANCE.removeNodeLabel(coordsInfo);
    }
    public void updateCoordsInfo() { coordsInfo.setText("(" + (int)getCenterX() + ", " + (int)getCenterY() + ")"); }
    public boolean areCoordsShowing() { return coordsShowing; }
    
    public void showNeighboursInfo() {
        if (neighboursShowing) return;
        neighboursShowing = true;
        neighboursInfo = new Label();
        neighboursInfo.setFont(INFO_FONT);
        updateNeighboursInfo();
        
        neighboursInfo.layoutXProperty().bind(centerXProperty().add(radiusProperty()).add(10));
        neighboursInfo.layoutYProperty().bind(centerYProperty().subtract(radiusProperty()).add(2*(EXTRA_INFO_VERTICAL_SPACING + INFO_FONT_SIZE)));
        GraphPane.INSTANCE.addNodeLabel(neighboursInfo);
    }
    public void hideNeighboursInfo() {
        neighboursShowing = false;
        if (neighboursInfo == null) return; // shouldn't really happen
        GraphPane.INSTANCE.removeNodeLabel(neighboursInfo);
    }
    public void updateNeighboursInfo() {
        if (!neighboursShowing) return;
        neighboursInfo.setText(getNeighbors().stream().map(n -> n.getNodeId()+"").collect(Collectors.joining(", ", "{", "}")));
    }
    public boolean areNeighboursShowing() { return neighboursShowing; }
    
    public void showStatesInfo() {
        if (statesShowing) return;
        statesShowing = true;
        statesInfo = new Label();
        statesInfo.setFont(INFO_FONT);
        updateStatesInfo();
        
        statesInfo.layoutXProperty().bind(centerXProperty().add(radiusProperty()).add(10));
        statesInfo.layoutYProperty().bind(centerYProperty().subtract(radiusProperty()).add(3*(EXTRA_INFO_VERTICAL_SPACING + INFO_FONT_SIZE)));
        GraphPane.INSTANCE.addNodeLabel(statesInfo);
    }
    public void hideStatesInfo() {
        statesShowing = false;
        if (statesInfo == null) return; // shouldn't really happen
        GraphPane.INSTANCE.removeNodeLabel(statesInfo);
    }
    public void updateStatesInfo() {
        if (!statesShowing) return;
        Platform.runLater(() -> statesInfo.setText(getStatesInfoString()));
    }
    public String getStatesInfoString() {
        if (WorkerController.totalStates == 1) return "[" + states.get(0).toString() + "]";
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        
        // current state is last state
        if (WorkerController.currentStateIndex == WorkerController.totalStates - 1) {
            for (; i<WorkerController.totalStates-1; i++) {
                sb.append(states.get(i).toString()).append(", ");
            }
            sb.append("|").append(states.get(i)).append("|]");
            return sb.toString();
        }
        
        // current state is somewhere in between
        for (; i<WorkerController.currentStateIndex; i++) {
            sb.append(states.get(i).toString()).append(", ");
        }
        sb.append("|").append(states.get(i++).toString()).append("|, ");
        for (; i<WorkerController.totalStates-1; i++) {
            sb.append(states.get(i).toString()).append(", ");
        }
        sb.append(states.get(i).toString()).append("]");
        return sb.toString();
    }
    public boolean areStatesShowing() { return statesShowing; }
    
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
    
    public void setNodeOpacity(double opacity) {
        Color clr = (Color) this.getFill();
        color = new Color(
                clr.getRed(),
                clr.getGreen(),
                clr.getBlue(),
                opacity);
        this.setFill(color);
    }
    
    public void setNodeColor(Color newColor) {
        color = newColor;
        this.setFill(color);
    }
    
    @Override
    public void onStateChange() {
        reflectCurrentStateIndex();
    }
    
    @Override
    public void onNewState(RoundStatisticsData roundStats) {
        // Do nothing
    }
    
    public int getNodeId() { return this.id; }
    
    public NodeState getState() { return this.states.get(WorkerController.currentStateIndex); }
    
    public void addState(NodeState nodeState) {
        this.states.add(nodeState);
    }
    
    public List<NodeState> getStates() { return this.states; }
    
    public static void setPosition(Node node, Point2D point2D) {
        node.setCenterX(point2D.getX());
        node.setCenterY(point2D.getY());
    }
    
    public static void setPositionSync(Node node, Point2D point2D) {
        Platform.runLater(() -> {
            node.setCenterX(point2D.getX());
            node.setCenterY(point2D.getY());
        });
    }
    
    public void setBatchParent(WorkBatch workBatch) { this.workBatch = workBatch; }
    public WorkBatch getBatchParent() { return workBatch; }
    
    // TODO how optimal is this?
    public List<Node> getNeighbors() {
        return Graphs.neighborListOf(MyGraph.getInstance().getGraph(), this);
//        return MyGraph.getInstance().getGraph().getEdgeTarget(MyGraph.getInstance().getGraph().edgesOf(this));
    }
    
    
    // equals & hashcode needed due to Node mutation
    // in order to make sure hashset operations work
    @Override
    public boolean equals(Object o) {
        if(o == null || o.getClass() != this.getClass()) {
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
