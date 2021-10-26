package com.example.gav_fx.panes;

import com.example.gav_fx.core.AlgorithmController;
import com.example.gav_fx.core.StateObserver;
import com.example.gav_fx.core.Tools;
import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class BottomPane extends Pane implements StateObserver {
    
    FlowPane buttonsContainer;
    
    private final Dimension2D BUTTON_SIZE = new Dimension2D(40, 30);
    final List<Button> stateButtons = new ArrayList<>(16);
    private Button highlightedButton; // indicating current state index
    
    public BottomPane() {
        this.setPrefHeight(200);
        this.getStyleClass().add("bottom-pane");
        
        buttonsContainer = new FlowPane();
        buttonsContainer.setPadding(new Insets(10));
        buttonsContainer.setHgap(5);
        buttonsContainer.setVgap(5);                                           // insets
        buttonsContainer.prefWrapLengthProperty().bind(this.widthProperty().subtract(20));
        
        highlightedButton = getNewStateButton(0);
        highlightedButton.setEffect(Tools.SHADOW_EFFECT_STATE_BTN);
        stateButtons.add(highlightedButton);
        
        buttonsContainer.getChildren().add(highlightedButton);
        this.getChildren().add(buttonsContainer);
        
        //this.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        //buttonsContainer.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    @Override
    public void onStateChange() {
        int index = AlgorithmController.currentStateIndex;
        highlightButton(stateButtons.get(index));
    }
    
    @Override
    public void onNewState() {
        int index = AlgorithmController.currentStateIndex;
        Button newBtn = getNewStateButton(index);
        stateButtons.add(newBtn);
    
        highlightButton(newBtn);
        Platform.runLater(() -> buttonsContainer.getChildren().add(newBtn));
    }
    
    private void highlightButton(Button b) {
        highlightedButton.setEffect(null);
        highlightedButton = b;
        highlightedButton.setEffect(Tools.SHADOW_EFFECT_STATE_BTN);
    }
    
    private Button getNewStateButton(int stateIndex) {
        Button btn = new Button(stateIndex + "");
        btn.setPrefSize(BUTTON_SIZE.getWidth(), BUTTON_SIZE.getHeight());
        btn.setOnAction(event -> {
            AlgorithmController.currentStateIndex = stateIndex;
            highlightButton(btn);
            
            // TODO setState in Node to index
            //MyGraph.getInstance().getNodes().forEach(n -> n.stat);
        });
        return btn;
    }
}
