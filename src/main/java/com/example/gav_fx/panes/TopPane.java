package com.example.gav_fx.panes;

import com.example.gav_fx.core.AlgorithmController;
import com.example.gav_fx.core.State;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

public class TopPane extends FlowPane {
    
    private final MyGraph graph = MyGraph.getInstance();
    
    public TopPane() {
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
    
            // when pressing continue, jump to the latest state
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
            Node newNode = MyGraph.getNode();
            newNode.setCenterX(50);
            newNode.setCenterY(50);
            
            // TODO should this be default behaviour in Node constructor?
            // when node is added in the middle of the simulation
            // prefill its history (state list) with uninformed states!
            // (one state is automatically made in constructor, so do 1 less)
            for (int i=0; i<AlgorithmController.currentStateIndex; i++) {
                newNode.addState(new State(0));
            }
            
            // TODO move this to some method in MyGraph, like onNodeRemoveOrAdd()
            //   same lambda is in deleteNodeBtn action method!
            // clear future history of states of nodes
            this.graph.getNodes().forEach(n -> {
                if (n.states.size() > AlgorithmController.totalStates) {
                    n.states.subList(AlgorithmController.totalStates, n.states.size()).clear();
                }
            });
    
            graph.addNode(newNode);
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
