package com.example.gav_fx.panes.optionpanes;

import javafx.scene.control.Label;

public class StaticTestGraphOptionPanel extends OptionPane {
    
    public StaticTestGraphOptionPanel() {
        Label l = new Label("STATIC IMPORT TEST");
        this.getChildren().add(l);
    }
}
