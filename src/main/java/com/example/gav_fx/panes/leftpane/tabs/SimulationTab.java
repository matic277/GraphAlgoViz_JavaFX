package com.example.gav_fx.panes.leftpane.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

public class SimulationTab extends TabContentComponent {
    
    public SimulationTab() {
        init();
    }
    
    private void init() {
        VBox threadsComponent = getThreadsComponent();
        this.getChildren().add(threadsComponent);
    }
    
    private VBox getThreadsComponent() {
        int cores = Runtime.getRuntime().availableProcessors();
        
        HBox titleContainer = getTitleContainer("Number of processors");
        
        Label inputLbl = new Label("Set custom amount:");
        
        TextField inputField = new TextField();
        Slider slider = new Slider();
        
        inputField.setText(cores + "");
        inputField.setMaxWidth(45);
        inputField.setMinWidth(35);
        inputField.setPrefHeight(25);
        inputField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER ) {
                int newVal = Integer.parseInt(inputField.getText());
                onThreadsChange(newVal);
                slider.setValue(newVal); // TODO: this invokes listener on bottom, which then rewrites value in field
            }
        } );
        
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
            onThreadsChange(intVal);
        });
    
        HBox inputContainer = new HBox();
        inputContainer.setSpacing(5);
        inputContainer.setPadding(new Insets(5, 5, 5, 5));
        inputContainer.getChildren().addAll(inputLbl, inputField);
        inputContainer.setAlignment(Pos.CENTER);
        
        VBox contentContainer = new VBox(inputContainer, slider);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        contentContainer.setAlignment(Pos.CENTER);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    public void onThreadsChange(int threads) {
    
    }
    
    @Override
    public String getTabName() {
        return "Simulation";
    }
}
