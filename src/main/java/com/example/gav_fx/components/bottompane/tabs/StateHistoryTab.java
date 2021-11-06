package com.example.gav_fx.components.bottompane.tabs;

import com.example.gav_fx.core.*;
import com.example.gav_fx.graph.Edge;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphVertexChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class StateHistoryTab extends Pane implements StateObserver, GraphChangeObserver {
    
    FlowPane buttonsContainer;
    
    private final Dimension2D BUTTON_SIZE = new Dimension2D(40, 30);
    final List<Button> stateButtons = new ArrayList<>(16);
    private Button highlightedButton; // indicating current state index
    
    public StateHistoryTab() {
        MyGraph.getInstance().addObserver(this);
        this.setPrefHeight(200);
        
        buttonsContainer = new FlowPane();
        buttonsContainer.setPadding(new Insets(10, 10, 10, 15));
        buttonsContainer.setHgap(5);
        buttonsContainer.setVgap(5);                                           // insets
        buttonsContainer.prefWrapLengthProperty().bind(this.widthProperty().subtract(20));
        //buttonsContainer.prefWidthProperty().bind(this.widthProperty());
    
        initOnGraphImport();
        
        this.getChildren().add(buttonsContainer);
        
        //this.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        //buttonsContainer.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    public void initOnGraphImport() {
        highlightedButton = getNewStateButton(0);
        highlightedButton.setEffect(Tools.SHADOW_EFFECT_STATE_BTN);
        stateButtons.add(highlightedButton);
        buttonsContainer.getChildren().add(highlightedButton);
    }
    
    @Override
    public void onStateChange() {
        // TODO not working correctly
        System.out.println("STATE CHANGE");
        int index = WorkerController.currentStateIndex;
        highlightButton(stateButtons.get(index));
    }
    
    @Override
    public void onNewState() {
        // TODO not working correctly
        System.out.println("NEW STATE");
        int index = WorkerController.currentStateIndex;
        Button newBtn = getNewStateButton(index);
        stateButtons.add(newBtn);
        
        highlightButton(newBtn);
        Platform.runLater(() -> buttonsContainer.getChildren().add(newBtn));
    }
    
    private void highlightButton(Button b) {
        if (highlightedButton == null) {
            LOG.error("HighlightedButton is null!");
            return;
        }
        highlightedButton.setEffect(null);
        highlightedButton = b;
        highlightedButton.setEffect(Tools.SHADOW_EFFECT_STATE_BTN);
    }
    
    private Button getNewStateButton(int stateIndex) {
        Button btn = new Button(stateIndex + "");
        btn.setPrefSize(BUTTON_SIZE.getWidth(), BUTTON_SIZE.getHeight());
        btn.setOnAction(event -> {
            WorkerController.currentStateIndex = stateIndex;
            highlightButton(btn);
        });
        return btn;
    }
    
    @Override
    public void onGraphClear() {
        buttonsContainer.getChildren().clear();
        stateButtons.clear();
        highlightedButton = null;
    }
    
    @Override
    public void onGraphImport() {
        initOnGraphImport();
    }
    
    @Override
    public void onNewInformedNode() {
    
    }
    
    @Override
    public void onNewUninformedNode() {
    
    }
    
    @Override
    public void edgeAdded(GraphEdgeChangeEvent<Node, Edge> e) {
    
    }
    
    @Override
    public void edgeRemoved(GraphEdgeChangeEvent<Node, Edge> e) {
    
    }
    
    @Override
    public void vertexAdded(GraphVertexChangeEvent<Node> e) {
    
    }
    
    @Override
    public void vertexRemoved(GraphVertexChangeEvent<Node> e) {
    
    }
}
