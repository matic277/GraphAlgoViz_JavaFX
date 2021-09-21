package com.example.gav_fx.panes;

import com.example.gav_fx.core.GraphChangeObserver;
import com.example.gav_fx.graph.Edge;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphVertexChangeEvent;

/**
 * PANNING AND ZOOMING
 * taken and adjusted from StackOverflow
 */
public class GraphPane extends Pane implements GraphChangeObserver {
    
    DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    
    Group edges;
    Group nodes;
    
    public static DoubleProperty OFFSET_X;
    public static DoubleProperty OFFSET_Y;
    
    public static GraphPane INSTANCE;
    
    public GraphPane() {
        INSTANCE = this;
        setPrefSize(600, 600);
        
        // TODO turn this off eventually
        // reference/context
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(50), BorderWidths.DEFAULT)));
        
        nodes = new Group();
        edges = new Group();
        
        this.getChildren().add(edges);
        this.getChildren().add(nodes);
        
        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
        
        MyGraph.getInstance().addObserver(this);
        
        OFFSET_X = this.translateXProperty();
        OFFSET_Y = this.translateYProperty();
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

