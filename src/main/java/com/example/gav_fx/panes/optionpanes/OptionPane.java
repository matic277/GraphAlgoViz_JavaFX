package com.example.gav_fx.panes.optionpanes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class OptionPane extends Pane {
    
    // Some common input fields
    public VBox getNodesInputContainer() {
        return getGenericInputContainer("Number of nodes");
    }
    
    public VBox getEdgeProbabilityInputContainer() {
        return getGenericInputContainer("Edge probability");
    }
    
    public VBox getGenericInputContainer(String inputTitle) {
        VBox container = new VBox();
        Label title = new Label(inputTitle);
        TextField inputField = new TextField();
        container.getChildren().add(title);
        container.getChildren().add(inputField);
        return container;
    }
}
