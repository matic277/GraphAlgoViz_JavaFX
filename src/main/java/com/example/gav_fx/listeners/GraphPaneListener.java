package com.example.gav_fx.listeners;

import com.example.gav_fx.panes.GraphPane;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Listeners for making the scene's canvas draggable and zoomable
 */
public class GraphPaneListener {
    
    private static final DragContext sceneDragContext = new DragContext();
    private GraphPane panAndZoomPane;
    
    public GraphPaneListener(GraphPane canvas) {
        this.panAndZoomPane = canvas;
    }
    
    public final EventHandler<MouseEvent> ON_MOUSE_CLICK = (event) -> {
        System.out.println("CLICK");
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                panAndZoomPane.resetZoom();
            }
        }
        if (event.getButton().equals(MouseButton.SECONDARY)) {
            if (event.getClickCount() == 2) {
                panAndZoomPane.fitWidth();
            }
        }
    };
    
    public final EventHandler<MouseEvent> ON_MOUSE_PRESS = (event) -> {
        sceneDragContext.mouseAnchorX = event.getX();
        sceneDragContext.mouseAnchorY = event.getY();
        
        sceneDragContext.translateAnchorX = panAndZoomPane.getTranslateX();
        sceneDragContext.translateAnchorY = panAndZoomPane.getTranslateY();
    };
    
    public final EventHandler<MouseEvent> ON_MOUSE_DRAG = (event) -> {
        panAndZoomPane.setTranslateX(sceneDragContext.translateAnchorX + event.getX() - sceneDragContext.mouseAnchorX);
        panAndZoomPane.setTranslateY(sceneDragContext.translateAnchorY + event.getY() - sceneDragContext.mouseAnchorY);
        event.consume();
    };
    
    public final EventHandler<ScrollEvent> ON_SCROLL = (event) -> {
        double delta = GraphPane.DEFAULT_DELTA;
        double scale = panAndZoomPane.getScale(); // currently we only use Y, same value is used for X
        double oldScale = scale;
        
        panAndZoomPane.setDeltaY(event.getDeltaY());
        if (panAndZoomPane.deltaY.get() < 0) {
            scale /= delta;
        } else {
            scale *= delta;
        }
        
        double f = (scale / oldScale)-1;
        double dx = (event.getX() - (panAndZoomPane.getBoundsInParent().getWidth()/2 + panAndZoomPane.getBoundsInParent().getMinX()));
        double dy = (event.getY() - (panAndZoomPane.getBoundsInParent().getHeight()/2 + panAndZoomPane.getBoundsInParent().getMinY()));
        
        panAndZoomPane.setPivot(f*dx, f*dy, scale);
        event.consume();
    };
}
