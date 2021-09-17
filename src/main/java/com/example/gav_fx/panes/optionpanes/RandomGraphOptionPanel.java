package com.example.gav_fx.panes.optionpanes;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class RandomGraphOptionPanel extends Pane {
    
    public RandomGraphOptionPanel() {
        Label l = new Label("RANDOM");
        this.getChildren().add(l);
    }
}
