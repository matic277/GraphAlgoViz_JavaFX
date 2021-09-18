package com.example.gav_fx.panes.optionpanes;

import javafx.scene.layout.VBox;

public class RandomGraphOptionPanel extends OptionPane {
    
    public RandomGraphOptionPanel() {
        //Label l = new Label("RANDOM");
        //this.getChildren().add(l);
        
        init();
    }
    
    private void init() {
        VBox container = new VBox();
        container.getChildren().add(getNodesInputContainer());
        container.getChildren().add(getEdgeProbabilityInputContainer());
        this.getChildren().add(container);
    }
}
