package com.example.gav_fx.panes;

import com.example.gav_fx.core.GraphChangeObserver;
import com.example.gav_fx.graph.Edge;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphVertexChangeEvent;

/**
 * PANNING AND ZOOMING
 * taken and adjusted from StackOverflow
 */
public class GraphPane extends Pane implements GraphChangeObserver {
    
    public static DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    
    Group edges;
    Group nodes;
    Group nodeLabels;
    
    public static DoubleProperty OFFSET_X;
    public static DoubleProperty OFFSET_Y;
    
    public static GraphPane INSTANCE;
    
    // Mouse tracking relative to this component
    public static final MouseLocation MOUSE_LOCATION = new MouseLocation();
    public static class MouseLocation  {
        public DoublePropertyBase x = new DoublePropertyBase() {
            @Override public Object getBean() { return GraphPane.MouseLocation.this; }
            @Override public String getName() { return "x"; }
        };
        public DoublePropertyBase y = new DoublePropertyBase() {
            @Override public Object getBean() { return GraphPane.MouseLocation.this; }
            @Override public String getName() { return "y"; }
        };
    }
    
    private Object clickedObjectSource;
    private final ContextMenu edgeMenu = new ContextMenu(); {
        MenuItem item = new MenuItem("Delete");
        item.setOnAction(e -> {
            MyGraph.getInstance().getGraph().removeEdge((Edge)clickedObjectSource);
            clickedObjectSource = null;
            e.consume();
        });
        edgeMenu.getItems().add(item);
    }
    
    private final MenuItem coordsItem = new MenuItem();
    private final MenuItem neighboursItem = new MenuItem();
    private final ContextMenu nodeMenu = new ContextMenu(); {
        MenuItem del = new MenuItem("Delete");
        del.setOnAction(e -> {
            MyGraph.getInstance().deleteNode((Node)clickedObjectSource);
            clickedObjectSource = null;
        });
        coordsItem.setOnAction(e -> {
            Node n = (Node)clickedObjectSource;
            if (n.areCoordsShowing()) n.hideCoordsInfo();
            else n.showCoordsInfo();
            clickedObjectSource = null;
        });
        neighboursItem.setOnAction(e -> {
            Node n = (Node)clickedObjectSource;
            if (n.areNeighboursShowing()) n.hideNeighboursInfo();
            else n.drawNeighboursInfo();
            clickedObjectSource = null;
        });
        nodeMenu.getItems().addAll(del, coordsItem, neighboursItem);
    }
    
    public GraphPane() {
        INSTANCE = this;
        //setPrefSize(600, 600);
        
        // reference/context
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
        
        //this.set
        
        nodes = new Group();
        edges = new Group();
        nodeLabels = new Group();
        
        this.getChildren().add(edges);
        this.getChildren().add(nodes);
        this.getChildren().add(nodeLabels);
        
        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
        
        MyGraph.getInstance().addObserver(this);
        
        OFFSET_X = this.translateXProperty();
        OFFSET_Y = this.translateYProperty();
        
        this.setOnMouseClicked(e -> {
            edgeMenu.hide();
            nodeMenu.hide();
            e.consume();
        });
    
        this.setOnMouseMoved(event -> {
            MOUSE_LOCATION.x.set(event.getX());
            MOUSE_LOCATION.y.set(event.getY());
        });
        
        this.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.autosize();
    }
    
    public void openContextMenuForEdge(ContextMenuEvent e) {
        clickedObjectSource = e.getSource();
        edgeMenu.show(this, e.getScreenX(), e.getScreenY());
    }
    
    public void openContextMenuForNode(ContextMenuEvent e) {
        clickedObjectSource = e.getSource();
        
        Node n = (Node)clickedObjectSource;
        coordsItem.setText((n.areCoordsShowing() ? "Hide" : "Show") + " coordinates");
        neighboursItem.setText((n.areCoordsShowing() ? "Hide" : "Show") + " neighbours");
        
        nodeMenu.show(this, e.getScreenX(), e.getScreenY());
    }
    
    public void addNodeLabel(Label lbl) {
        nodeLabels.getChildren().add(lbl);
    }
    
    public void removeNodeLabel(Label lbl) {
        nodeLabels.getChildren().remove(lbl);
    }
    
    @Override
    public void onGraphClear() {
        edges.getChildren().clear();
        nodes.getChildren().clear();
    }
    
    @Override
    public void onGraphImport() {
    
    }
    
    @Override
    public void edgeAdded(GraphEdgeChangeEvent<Node, Edge> event) {
        edges.getChildren().add(event.getEdge().getLine());
    }
    
    @Override
    public void edgeRemoved(GraphEdgeChangeEvent<Node, Edge> event) {
        edges.getChildren().remove(event.getEdge().getLine());
    }
    
    @Override
    public void vertexAdded(GraphVertexChangeEvent<Node> event) {
        nodes.getChildren().add(event.getVertex());
    }
    
    @Override
    public void vertexRemoved(GraphVertexChangeEvent<Node> event) {
        nodes.getChildren().remove(event.getVertex());
    }
    
    @Override public void onNewInformedNode() {}
    @Override public void onNewUninformedNode() {}
    
    
    
    public double getScale() { return myScale.get(); }
    public void setScale( double scale) { myScale.set(scale); }
    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}

