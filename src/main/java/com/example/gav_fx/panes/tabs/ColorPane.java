package com.example.gav_fx.panes.tabs;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class ColorPane extends Pane {
    
    public ColorPane() {
        Label l = new Label("colors");
        this.getChildren().add(l);
    }
}
