package com.example.gav_fx.components.leftpane.tabs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class LogTab extends ScrollPane {
    
    public static final LogTab INSTANCE = new LogTab(); // Easiest way to solve this (visibility between LOG and this), but is it good?
    
    public TextFlow textArea;
    
    public LogTab() {
        this.textArea = new TextFlow();
        this.setContent(textArea);
        this.setPadding(new Insets(15, 5, 5, 5));
    }
    
    // TODO call to this for some reason triggers a bunch of warning for class cast exception
    //   seems to be caused by extends ScrollPane, idk why this is happening.
    public static synchronized void logText(String logText, Color color) {
        Text text = new Text(logText + "\n");
        text.setFont(new Font("Consolas", 12));
        text.setFill(color);
        
        // fixes: Not on FX application thread; error
        Platform.runLater(() -> {
            INSTANCE.textArea.getChildren().add(text);
        });
    }
    
    public String getTabName() { return "LOG"; }
}
