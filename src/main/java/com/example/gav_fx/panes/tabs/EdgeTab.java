package com.example.gav_fx.panes.tabs;

import com.example.gav_fx.graph.MyGraph;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class EdgeTab extends TabElement {
    
    public EdgeTab() {
        init();
    }
    
    private void init() {
        VBox widthComponent = getEdgeWidthComponent();
        VBox colorComponent = getEdgeColorComponent();
        VBox opacityComponent = getOpacityComponent();
        
        this.getChildren().addAll(widthComponent, colorComponent, opacityComponent);
    }
    
    private VBox getOpacityComponent() {
        Label title = new Label("Set edge opacity");
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(1);
        slider.setMin(0);
        slider.setValue(1); // default
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getGraph().edgeSet().forEach(e -> e.setEdgeOpacity(slider.getValue()));
        });
    
        VBox container = new VBox();
        container.getChildren().add(title);
        container.getChildren().add(slider);
        return container;
    }
    
    
    private VBox getEdgeWidthComponent() {
        Label title = new Label("Set edge width");
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(30);
        slider.setMin(0);
        slider.setMajorTickUnit(10);
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getGraph().edgeSet().forEach(e -> e.setEdgeWidth(slider.getValue()));
        });
        
        VBox container = new VBox();
        container.getChildren().add(title);
        container.getChildren().add(slider);
        return container;
    }
    
    private VBox getEdgeColorComponent() {
        Label title = new Label("Set edge color");
        ColorPicker clrPicker = new ColorPicker();
        clrPicker.setOnAction(event -> {
            MyGraph.getInstance().getGraph().edgeSet().forEach(e -> e.setEdgeColor(clrPicker.getValue()));
        });
        
        VBox container = new VBox();
        container.getChildren().add(title);
        container.getChildren().add(clrPicker);
        return container;
    }
    
    @Override
    public String getTabName() { return "Edges"; }
}
