package com.example.gav_fx.panes;

import com.example.gav_fx.App;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class RightPane extends Pane {
    
    private final Label mouseLocationIndicator;
    
    public RightPane() {
        this.setMinWidth(100);
        this.getStyleClass().add("right-pane");
        
        mouseLocationIndicator = new Label();
        HBox contentContainer = new HBox();
        contentContainer.setPadding(new Insets(10, 10, 10, 10));
        contentContainer.getChildren().add(mouseLocationIndicator);
        
        this.getChildren().add(contentContainer);
    }
    
    public void updateMousePosition() {
        mouseLocationIndicator.setText(
                "(" +
                (int) App.MOUSE_LOCATION.x.get() + ", " +
                (int) App.MOUSE_LOCATION.y.get() +
                ")");
    }
}
