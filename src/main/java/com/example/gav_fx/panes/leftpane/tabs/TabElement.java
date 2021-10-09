package com.example.gav_fx.panes.leftpane.tabs;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public abstract class TabElement extends VBox {
    
    public TabElement() {
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setSpacing(20);
    }
    
    public abstract String getTabName();
}
