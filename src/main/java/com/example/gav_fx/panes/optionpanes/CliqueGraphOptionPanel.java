package com.example.gav_fx.panes.optionpanes;

import javafx.scene.layout.VBox;

public class CliqueGraphOptionPanel extends OptionPane {
    
    public CliqueGraphOptionPanel() {
        init();
    }
    
    private void init() {
        VBox container = new VBox();
        container.getChildren().add(getNodesInputContainer());
        this.getChildren().add(container);
    }
}
