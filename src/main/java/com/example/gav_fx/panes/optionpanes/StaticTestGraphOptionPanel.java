package com.example.gav_fx.panes.optionpanes;

import com.example.gav_fx.core.LayoutType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class StaticTestGraphOptionPanel extends Pane {
    
    public StaticTestGraphOptionPanel() {
        Label l = new Label("STATIC IMPORT TEST");
        this.getChildren().add(l);
    }
}
