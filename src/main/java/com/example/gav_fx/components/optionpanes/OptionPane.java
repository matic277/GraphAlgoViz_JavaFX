package com.example.gav_fx.components.optionpanes;

import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.function.Function;

public abstract class OptionPane extends Pane {
    
    // Some common input fields
    public VBox getNodesInputContainer() {
        return getGenericInputContainer("Number of nodes");
    }
    
    public VBox getEdgeProbabilityInputContainer() {
        return getGenericInputContainer("Edge probability");
    }
    
    public VBox getInformedProbabilityContainer() {
        return getGenericInputContainer("Informed probability");
    }
    
    // Vbox.getChildren().get(1) should return the input field obj
    public VBox getGenericInputContainer(String inputTitle) {
        VBox container = new VBox();
        Label title = new Label(inputTitle);
        TextField inputField = new TextField();
        container.getChildren().add(title);
        container.getChildren().add(inputField);
        return container;
    }
    
    public abstract Function<ImportType, GraphBuilder> getBuilder();
}
