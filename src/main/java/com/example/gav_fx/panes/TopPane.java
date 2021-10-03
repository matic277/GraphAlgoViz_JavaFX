package com.example.gav_fx.panes;

import com.example.gav_fx.core.AlgorithmController;
import com.example.gav_fx.core.State;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;

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
        SVGPath runSvg = new SVGPath();
        runSvg.setContent("M4,2L14,8L4,14Z");
        runSvg.setFill(new Color(0.35,0.66,0.41, 1));
        SVGPath pauseSvg = new SVGPath();
        pauseSvg.setContent("M0,0L10,0L10,10L0,10L0,0Z");
        pauseSvg.setFill(new Color(0.86,0.35,0.38, 1));
        
        Button btn = new Button();
        //BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("play.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        //Background background = new Background(backgroundImage);
        //btn.setBackground(background);
        //btn.getStyleClass().add("icon-button-run");
        btn.setGraphic(runSvg);
    
        btn.setPrefSize(30, 30);
    
        System.out.println(btn.getPrefWidth());
        runSvg.setScaleX(2);
        runSvg.setScaleY(2);
        pauseSvg.setScaleX(2);
        pauseSvg.setScaleY(2);
        
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
            
            btn.setGraphic(AlgorithmController.PAUSE.get() ? runSvg : pauseSvg);
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
        SVGPath svg = new SVGPath();
        svg.setContent(
                "M2,3L5.5,3L7,5L9,5L9,9L13,9L13,5L14,5L14,13L2,13Z" +
                "M12,4L12,8L10,8L10,4L8,4L11,1L14,4L12,4Z");
        
        Button btn = new Button();
        btn.setPrefSize(30, 30);
        //btn.getStyleClass().add("icon-button-import");
        btn.setGraphic(svg);
    
        System.out.println(btn.getPrefWidth());
        //btn.setScaleX(2);
        //btn.setScaleY(2);
        svg.setScaleX(2);
        svg.setScaleY(2);
        
        btn.setOnMouseClicked(event -> {
            if (ImportGraphPane.isOpened) return;
            Parent root = new ImportGraphPane(this);
        });
        
        this.getChildren().add(btn);
    }
}
