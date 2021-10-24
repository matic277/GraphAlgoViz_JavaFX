package com.example.gav_fx.panes;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class BottomPane extends Pane {
    
    public BottomPane() {
        this.setPrefHeight(200);
        this.getStyleClass().add("bottom-pane");
        //this.setPadding(new Insets(10, 10, 10, 10));
    
        HBox b = new HBox();
        b.setPadding(new Insets(10, 10, 10, 10));
    
        Label l = new Label("bottom");
        b.getChildren().add(l);
        this.getChildren().add(b);
        //this.get
    }
}
