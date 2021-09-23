package com.example.gav_fx.panes.tabs;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class SimulationTab extends TabElement {
    
    public SimulationTab() {
        init();
    
        
    }
    
    private void init() {
        VBox threadsComponent = getThreadsComponent();
        this.getChildren().add(threadsComponent);
    }
    
    private VBox getThreadsComponent() {
        int cores = Runtime.getRuntime().availableProcessors();
        
        Label title = new Label("Set number of threads");
        title.setPadding(new Insets(0, 10, 0, 5));
        //title.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(2), BorderWidths.DEFAULT)));
        TextField inputField = new TextField();
        inputField.setText(cores + "");
        inputField.setMaxWidth(45);
        inputField.setPrefHeight(25);
        title.setPrefHeight(25);
        
        
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(9);
        slider.setMax(20);
        slider.setMin(1);
        slider.setValue(cores); // default
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            // make a slider move in int values (not double), TODO maybe there is a better way to do this
            int intVal = (int) Math.round(slider.getValue());
            slider.setValue(intVal);
            inputField.setText(intVal + "");
            
            // TODO actual impl
        });
    
        HBox inputContainer = new HBox();
        inputContainer.setPadding(new Insets(0, 0, 5, 0));
        inputContainer.getChildren().addAll(title, inputField);
        
        VBox container = new VBox();
        container.getChildren().addAll(inputContainer, slider);
        
        return container;
    }
    
    @Override
    public String getTabName() {
        return "Simulation";
    }
}
