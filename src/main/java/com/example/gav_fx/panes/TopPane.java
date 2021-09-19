package com.example.gav_fx.panes;

import com.example.gav_fx.core.AlgorithmController;
import com.example.gav_fx.core.AlgorithmExecutor;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TopPane extends FlowPane {
    
    private final MyGraph graph = MyGraph.getInstance();
    
    public TopPane() {
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTSEAGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        Label l = new Label("top");
        this.getChildren().add(l);
        
        this.setAlignment(Pos.CENTER);
        
        initRunAlgorithmButton();
        initImportButton();
        initDeleteGraphButton();
        initAddNodeButton();
    }
    
    private void initRunAlgorithmButton() {
        Button btn = new Button("run");
        btn.setOnMouseClicked(event -> {
            // Thread safe atomic boolean flip
            // flip the value of PAUSE
            boolean temp;
            do { temp = AlgorithmController.PAUSE.get(); }
            while (!AlgorithmController.PAUSE.compareAndSet(temp, !temp));
    
            // when pressing continue, jump to latest state
            // TODO
            //  program crash due to node getting drawn from  state 1 when only 1 state existed
            //  crashed on node.draw on line
            //  g.setColor(states.get(AlgorithmController.currentStateIndex).getState() == 0 ? UNINFORMED : INFORMED);
            //  (index 1 out of range of size of list 1)
            //  -> Happened once, can't reproduce.
            AlgorithmController.currentStateIndex = AlgorithmController.totalStates - 1;
    
            synchronized (AlgorithmController.PAUSE_LOCK) {
                AlgorithmController.PAUSE_LOCK.notify();
            }
        });
        
        this.getChildren().add(btn);
    }
    
    private void initAddNodeButton() {
        Button btn = new Button("add node");
        btn.setOnMouseClicked(event -> {
            //graph.addNode();
        });
        
        this.getChildren().add(btn);
    }
    
    private void initDeleteGraphButton() {
        Button btn = new Button("delete");
        btn.setOnMouseClicked(event -> {
            graph.clearGraph();
        });
        
        this.getChildren().add(btn);
    }
    
    //FlatSVGIcon importIcon = new FlatSVGIcon("icons/flatlaf/FileChooserUpFolderIcon.svg").derive(iconSize.width, iconSize.height);
    private void initImportButton() {
        Button btn = new Button("import");
        //btn.getStyleClass().add("icon-button");
        btn.setOnMouseClicked(event -> {
            if (ImportGraphPane.isOpened) return;
            Parent root = new ImportGraphPane(this);
        });
        
        this.getChildren().add(btn);
    }
}
