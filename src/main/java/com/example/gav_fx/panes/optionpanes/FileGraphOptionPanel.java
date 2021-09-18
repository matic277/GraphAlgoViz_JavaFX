package com.example.gav_fx.panes.optionpanes;

import javafx.scene.layout.VBox;

public class FileGraphOptionPanel extends OptionPane {
    
    public FileGraphOptionPanel() {
        init();
    }
    
    private void init() {
        VBox container = new VBox();
        container.getChildren().add(getGenericInputContainer("Path to graph file"));
        this.getChildren().add(container);
    }
}
