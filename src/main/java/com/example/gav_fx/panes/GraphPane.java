package com.example.gav_fx.panes;

import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.listeners.GraphPaneListener;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class GraphPane extends Pane {
    
    private final ScrollPane parentPane;
    
    private final Timeline timeline;
    
    public static final double DEFAULT_DELTA = 1.3d;
    public final DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    public final DoubleProperty deltaY = new SimpleDoubleProperty(0.0);
    
    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0d);
    
    public GraphPane(ScrollPane parentPane) {
        this.parentPane = parentPane;
        this.timeline = new Timeline(60);
        
        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
        
        MyGraph.getInstance().setGraphPane(this);
        
        GraphPaneListener sceneGestures = new GraphPaneListener(this);
        
        parentPane.addEventFilter(MouseEvent.MOUSE_CLICKED, sceneGestures.ON_MOUSE_CLICK);
        parentPane.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.ON_MOUSE_PRESS);
        parentPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.ON_MOUSE_DRAG);
        parentPane.addEventFilter(ScrollEvent.ANY,          sceneGestures.ON_SCROLL);
    
        zoomProperty.bind(this.myScale);
        
        parentPane.setContent(this);
        this.toBack();
    }
    
    public void setPivot( double x, double y, double scale) {
        // note: pivot value must be untransformed, i. e. without scaling
        // timeline that scales and moves the node
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200), new KeyValue(translateXProperty(), getTranslateX() - x)),
                new KeyFrame(Duration.millis(200), new KeyValue(translateYProperty(), getTranslateY() - y)),
                new KeyFrame(Duration.millis(200), new KeyValue(myScale, scale))
        );
        timeline.play();
    }
    
    public void fitWidth () {
        double scale = getParent().getLayoutBounds().getMaxX()/getLayoutBounds().getMaxX();
        double oldScale = getScale();
        
        double f = scale - oldScale;
        
        double dx = getTranslateX() - getBoundsInParent().getMinX() - getBoundsInParent().getWidth()/2;
        double dy = getTranslateY() - getBoundsInParent().getMinY() - getBoundsInParent().getHeight()/2;
        
        double newX = f*dx + getBoundsInParent().getMinX();
        double newY = f*dy + getBoundsInParent().getMinY();
        
        setPivot(newX, newY, scale);
    }
    
    public void resetZoom () {
        double scale = 1.0d;
        double x = getTranslateX();
        double y = getTranslateY();
        setPivot(x, y, scale);
    }
    
    public double getDeltaY() { return deltaY.get(); }
    public void setDeltaY( double dY) { deltaY.set(dY); }
    
    public double getScale() { return myScale.get(); }
    public void setScale(double scale) { myScale.set(scale); }
}