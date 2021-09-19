package com.example.gav_fx.panes.tabs;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class HistoryPane extends Pane {
    
    public HistoryPane() {
        Label l = new Label("history");
        this.getChildren().add(l);
    }
}
