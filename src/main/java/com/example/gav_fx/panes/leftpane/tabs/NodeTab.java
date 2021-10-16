package com.example.gav_fx.panes.leftpane.tabs;

import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
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
        HBox titleContainer = getTitleContainer("Set node radius");
        
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(100);
        slider.setMin(0);
        slider.setMajorTickUnit(10);
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getNodes().forEach(n -> n.setNewRadius(slider.getValue()));
        });
        
        VBox contentContainer = new VBox(slider);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private VBox getBorderColorComponent() {
        HBox titleContainer = getTitleContainer("Set border color");
        
        ColorPicker clrPicker = new ColorPicker();
        // TODO can all these be binds instead of listening?
        //  same for other eventHandlers in this class
        clrPicker.setOnAction(event -> {
            MyGraph.getInstance().getNodes().forEach(n -> n.setNewBorderColor(clrPicker.getValue()));
        });
    
        VBox contentContainer = new VBox(clrPicker);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        contentContainer.setAlignment(Pos.CENTER);
    
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private VBox getBorderWidthComponent() {
        HBox titleContainer = getTitleContainer("Set border width");
        
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(30);
        slider.setMin(0);
        slider.setMajorTickUnit(10);
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getNodes().forEach(n -> n.setNewBorderWidth(slider.getValue()));
        });
        slider.setValue(Node.BORDER_WIDTH); // default
        
        VBox contentContainer = new VBox(slider);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        contentContainer.setAlignment(Pos.CENTER);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    @Override
    public String getTabName() { return "Nodes"; }
}
