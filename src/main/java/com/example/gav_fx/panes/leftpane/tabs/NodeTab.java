package com.example.gav_fx.panes.leftpane.tabs;

import com.example.gav_fx.graph.MyGraph;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class NodeTab extends TabElement {
    
    public NodeTab() {
        init();
    }
    
    private void init() {
        VBox clrComponent = getBorderColorComponent();
        VBox widthComponent = getBorderWidthComponent();
        VBox radiusComponent = getNodeRadiusComponent();
        
        this.getChildren().addAll(clrComponent, widthComponent, radiusComponent);
    }
    
    private VBox getNodeRadiusComponent() {
        Label title = new Label("Set node radius");
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(100);
        slider.setMin(0);
        slider.setMajorTickUnit(10);
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getNodes().forEach(n -> n.setNewRadius(slider.getValue()));
        });
        
        VBox container = new VBox();
        container.getChildren().add(title);
        container.getChildren().add(slider);
        return container;
    }
    
    private VBox getBorderColorComponent() {
        Label title = new Label("Set border color");
        ColorPicker clrPicker = new ColorPicker();
        
        // TODO can all these be binds instead of listening?
        //  same for other eventHandlers in this class
        clrPicker.setOnAction(event -> {
            MyGraph.getInstance().getNodes().forEach(n -> n.setNewBorderColor(clrPicker.getValue()));
        });
        
        VBox container = new VBox();
        container.getChildren().add(title);
        container.getChildren().add(clrPicker);
        return container;
    }
    
    private VBox getBorderWidthComponent() {
        Label title = new Label("Set border width");
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(30);
        slider.setMin(0);
        slider.setMajorTickUnit(10);
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getNodes().forEach(n -> n.setNewBorderWidth(slider.getValue()));
        });
    
        VBox container = new VBox();
        container.getChildren().add(title);
        container.getChildren().add(slider);
        return container;
    }
    
    @Override
    public String getTabName() { return "Nodes"; }
}
