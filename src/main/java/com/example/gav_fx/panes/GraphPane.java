package com.example.gav_fx.panes;

import com.example.gav_fx.core.GraphChangeObserver;
import com.example.gav_fx.graph.Edge;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphVertexChangeEvent;

/**
 * PANNING AND ZOOMING
 * taken and adjusted from StackOverflow
 */
public class GraphPane extends Pane implements GraphChangeObserver {
    
    DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    
    public GraphPane() {
        setPrefSize(600, 600);
    
        // TODO turn this off eventually
        // reference/context
        this.setBorder(new Border(new BorderStroke(Color.LIGHTCORAL, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        
        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
        
        MyGraph.getInstance().addObserver(this);
    }
    
    
    @Override
    public void onGraphClear() {
        this.getChildren().clear();
    }
    
    @Override
    public void onGraphImport() {
        // TODO
    }
    
    @Override
    public void onNewInformedNode() {
    
    }
    
    @Override
    public void onNewUninformedNode() {
    
    }
    
    @Override
    public void edgeAdded(GraphEdgeChangeEvent<Node, Edge> event) {
        Line edgeDrawable = event.getEdge().getLine();
        this.getChildren().add(edgeDrawable);
        
        edgeDrawable.toBack();
    }
    
    @Override
    public void edgeRemoved(GraphEdgeChangeEvent<Node, Edge> event) {
        this.getChildren().remove(event.getEdge().getLine());
    }
    
    @Override
    public void vertexAdded(GraphVertexChangeEvent<Node> event) {
        Node node = event.getVertex();
        this.getChildren().add(node);
        
        node.toFront();
    }
    
    @Override
    public void vertexRemoved(GraphVertexChangeEvent<Node> event) {
        this.getChildren().remove(event.getVertex());
    }
    
    
    
    
    
    
    
    
    public double getScale() {
        return myScale.get();
    }
    
    public void setScale( double scale) {
        myScale.set(scale);
    }
    
    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}

