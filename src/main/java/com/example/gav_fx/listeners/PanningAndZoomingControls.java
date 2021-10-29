package com.example.gav_fx.listeners;

import com.example.gav_fx.graph.Node;
import com.example.gav_fx.components.GraphPane;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class PanningAndZoomingControls {
    
    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = .1d;
    
    private final DragContext sceneDragContext = new DragContext();
    public static class DragContext {
        double mouseAnchorX;
        double mouseAnchorY;
        
        double translateAnchorX;
        double translateAnchorY;
    }
    
    private GraphPane canvas;
    
    public PanningAndZoomingControls(GraphPane canvas) {
        this.canvas = canvas;
    }
    
    public EventHandler<MouseEvent> getOnMousePressEventHandler() { return ON_MOUSE_PRESS; }
    private final EventHandler<MouseEvent> ON_MOUSE_PRESS = event -> {
        sceneDragContext.mouseAnchorX = event.getSceneX();
        sceneDragContext.mouseAnchorY = event.getSceneY();
        sceneDragContext.translateAnchorX = canvas.getTranslateX();
        sceneDragContext.translateAnchorY = canvas.getTranslateY();
        
        // Clicked on "background", stop edge-drawing mode
        GraphPane.INSTANCE.getChildren().remove(Node.edgeRef.get());
        Node.clickedNodeRef.set(null);
        Node.edgeRef.set(null);
    };
    
    public EventHandler<MouseEvent> getOnMouseDragEventHandler() { return ON_MOUSE_DRAG; }
    private final EventHandler<MouseEvent> ON_MOUSE_DRAG = event -> {
        canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
        canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);
        event.consume();
    };
    
    public EventHandler<ScrollEvent> getOnScrollEventHandler() { return ON_SCROLL; }
    private final EventHandler<ScrollEvent> ON_SCROLL = event -> {
        double delta = 1.2;
        
        double scale = canvas.getScale(); // currently, we only use Y, same value is used for X
        double oldScale = scale;
        
        if (event.getDeltaY() < 0) scale /= delta;
        else scale *= delta;
        scale = clamp( scale, MIN_SCALE, MAX_SCALE);
        
        double f = (scale / oldScale)-1;
        
        double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 + canvas.getBoundsInParent().getMinX()));
        double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 + canvas.getBoundsInParent().getMinY()));
        
        canvas.setScale( scale);
        
        // note: pivot value must be untransformed, i.e. without scaling
        canvas.setPivot(f*dx, f*dy);
        
        event.consume();
    };
    
    public static double clamp( double value, double min, double max) {
        if(Double.compare(value, min) < 0) return min;
        if(Double.compare(value, max) > 0) return max;
        return value;
    }
}