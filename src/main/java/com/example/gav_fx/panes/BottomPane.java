package com.example.gav_fx.panes;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class BottomPane extends Pane {
    
    public BottomPane() {
        Label l = new Label("bottom");
        this.getChildren().add(l);
    }
}
