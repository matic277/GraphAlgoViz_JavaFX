package com.example.gav_fx.components.toppane;

import com.example.gav_fx.core.WorkerController;
import com.example.gav_fx.core.NodeState;
import com.example.gav_fx.core.Tools;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import com.example.gav_fx.components.ImportGraphPane;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Transform;

public class TopPane extends FlowPane {
    
    private final MyGraph graph = MyGraph.getInstance();
    private final WorkerController workerController;
    
    private static final Dimension2D BUTTON_SIZE = new Dimension2D(33, 33);
    
    public TopPane(WorkerController workerController) {
        this.workerController = workerController;
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("top-pane");
        
        init();
    }
    
    private void init() {
        Button runBtn = getRunButton();
        Button prevBtn = getPreviousStateButton();
        Button nextBtn = getNextStateButton();
        Button importBtn = getImportButton();
        Button deleteBtn = getDeleteGraphButton();
        Button addNodeBtn = getAddNodeButton();
        
        ImportComponent importCmp = new ImportComponent(workerController);
        
        HBox container = new HBox();
        container.setSpacing(5);
        container.setPadding(new Insets(5, 0, 5, 0));
        container.getChildren().addAll(
                importCmp,
                runBtn,
                prevBtn,
                nextBtn,
                importBtn,
                deleteBtn,
                addNodeBtn);
        this.getChildren().add(container);
    }
    
    private Button getRunButton() {
        SVGPath runSvg = new SVGPath();
        runSvg.setContent("M4,2L14,8L4,14Z");
        runSvg.setFill(new Color(0.35,0.66,0.41, 1));
        runSvg.setScaleX(1.6);
        runSvg.setScaleY(1.6);
        SVGPath pauseSvg = new SVGPath();
        pauseSvg.setContent("M0,0L10,0L10,10L0,10L0,0Z");
        pauseSvg.setFill(new Color(0.86,0.35,0.38, 1));
        pauseSvg.setScaleX(1.6);
        pauseSvg.setScaleY(1.6);
        
        Button btn = new Button();
        btn.getStyleClass().add("top-button");
        btn.setPrefSize(BUTTON_SIZE.getWidth(), BUTTON_SIZE.getHeight());
        btn.setGraphic(runSvg);
        
        
        btn.setOnMouseClicked(event -> {
            // Thread safe atomic boolean flip
            // flip the value of PAUSE
            boolean temp;
            do { temp = WorkerController.PAUSE.get(); }
            while (!WorkerController.PAUSE.compareAndSet(temp, !temp));
    
            // when pressing continue, jump to the latest state
            WorkerController.currentStateIndex = WorkerController.totalStates - 1;
    
            synchronized (WorkerController.PAUSE_LOCK) {
                WorkerController.PAUSE_LOCK.notify();
            }
            
            btn.setGraphic(WorkerController.PAUSE.get() ? runSvg : pauseSvg);
        });
        
        return btn;
    }
    
    private Button getNextStateButton() {
        SVGPath p1 = new SVGPath();
        p1.setContent("M0,0L11,0L11,2L0,2L0,0Z");
        p1.getTransforms().add(Transform.affine(-1, 0, 0, 1, 13, 7));
        
        SVGPath p2 = new SVGPath();
        p2.setContent("M0,0L7,0L7,1.8L0,1.8L0,0Z");
        p2.getTransforms().add(Transform.affine(0.707107, -0.707107, 0.707107, 0.707107, -0.000265, 4.978474));
        
        SVGPath p3 = new SVGPath();
        p3.setContent("M0,0L1.8,0L1.8,7L0,7L0,0Z");
        p3.getTransforms().add(Transform.affine(0.707107, -0.707107, 0.707107, 0.707107, -0.000274, 4.97852));
        
        Shape subShape = Shape.union(p2, p3);
        subShape.getTransforms().add(Transform.affine(-1, 0, 0, 1, 15, 3.02));
        
        Shape mainShape = Shape.union(p1, subShape);
        
        mainShape.setScaleX(1.25);
        mainShape.setScaleY(1.25);
        mainShape.setFill(Tools.ICON_COLOR);
    
        Button btn = new Button();
        btn.getStyleClass().add("top-button");
        btn.setPrefSize(BUTTON_SIZE.getWidth(), BUTTON_SIZE.getHeight());
        btn.setGraphic(mainShape);
    
        btn.setOnMouseClicked(event -> {
        
        });
    
        return btn;
    }
    
    private Button getPreviousStateButton() {
        SVGPath p1 = new SVGPath();
        p1.setContent("M0,0L12,0L12,2L0,2L0,0Z");
        p1.getTransforms().add(Transform.affine(1, 0, 0, 1, 1, 4));
    
        SVGPath p2 = new SVGPath();
        p2.setContent("M0,0L7,0L7,1.8L0,1.8L0,0Z");
        p2.getTransforms().add(Transform.affine(0.707107, -0.707107, 0.707107, 0.707107, -0.000265, 4.978474));
    
        SVGPath p3 = new SVGPath();
        p3.setContent("M0,0L1.8,0L1.8,7L0,7L0,0Z");
        p3.getTransforms().add(Transform.affine(0.707107, -0.707107, 0.707107, 0.707107, -0.000274, 4.97852));
        
        Shape subShape = Shape.union(p2, p3);
        subShape.getTransforms().add(Transform.affine(1, 0, 0, 1, 0, 0.02));
        
        Shape mainShape = Shape.union(p1, subShape);
        mainShape.getTransforms().add(Transform.affine(1, 0, 0, 1, 1, 0));
        
        mainShape.setScaleX(1.25);
        mainShape.setScaleY(1.25);
        mainShape.setFill(Tools.ICON_COLOR);
    
        Button btn = new Button();
        btn.getStyleClass().add("top-button");
        btn.setPrefSize(BUTTON_SIZE.getWidth(), BUTTON_SIZE.getHeight());
        btn.setGraphic(mainShape);
        
        btn.setOnMouseClicked(event -> {
        
        });
        
        return btn;
    }
    
    private Button getAddNodeButton() {
        SVGPath p1 = new SVGPath();
        p1.setContent("M7 3H9V13H7z");
        
        SVGPath p2 = new SVGPath();
        p2.setContent("M7 3H9V13H7z");
        p2.getTransforms().add(Transform.rotate(90, 8, 8));
        
        Shape mainShape = Shape.union(p1, p2);
        mainShape.setFill(Tools.ICON_COLOR);
        mainShape.setScaleX(1.3);
        mainShape.setScaleY(1.3);
        
        // OLD
        //SVGPath addSvg = new SVGPath();
        //addSvg.setContent("M 7.5 1 C 3.9160714 1 1 3.9160714 1 7.5 C 1 11.083929 3.9160714 14 7.5 14 C 11.083929 14 14 11.083929 14 7.5 C 14 3.9160714 11.083929 1 7.5 1 z M 7.5 2 C 10.543488 2 13 4.4565116 13 7.5 C 13 10.543488 10.543488 13 7.5 13 C 4.4565116 13 2 10.543488 2 7.5 C 2 4.4565116 4.4565116 2 7.5 2 z M 7 5 L 7 5.5 L 7 7 L 5.5 7 L 5 7 L 5 8 L 5.5 8 L 7 8 L 7 9.5 L 7 10 L 8 10 L 8 9.5 L 8 8 L 9.5 8 L 10 8 L 10 7 L 9.5 7 L 8 7 L 8 5.5 L 8 5 L 7 5 z");
        //addSvg.setFill(Color.BLACK);
        //addSvg.setScaleX(1.6);
        //addSvg.setScaleY(1.6);
        
        Button btn = new Button();
        btn.getStyleClass().add("top-button");
        btn.setGraphic(mainShape);
        btn.setPrefSize(BUTTON_SIZE.getWidth(), BUTTON_SIZE.getHeight());
        btn.setOnMouseClicked(event -> {
            Node newNode = MyGraph.getNode();
            newNode.setCenterX(50);
            newNode.setCenterY(50);
            
            // TODO should this be default behaviour in Node constructor?
            // when node is added in the middle of the simulation
            // prefill its history (state list) with uninformed states!
            // (one state is automatically made in constructor, so do 1 less)
            for (int i = 0; i< WorkerController.currentStateIndex; i++) {
                newNode.addState(new NodeState(0));
            }
            
            // TODO move this to some method in MyGraph, like onNodeRemoveOrAdd()
            //   same lambda is in deleteNodeBtn action method!
            // clear future history of states of nodes
            this.graph.getNodes().forEach(n -> {
                if (n.nodeStates.size() > WorkerController.totalStates) {
                    n.nodeStates.subList(WorkerController.totalStates, n.nodeStates.size()).clear();
                }
            });
            
            graph.addNode(newNode);
        });
        
        return btn;
    }

    private Button getDeleteGraphButton() {
        // not working - for some reason button gets resized ????????
        //SVGPath svg = new SVGPath();
        //svg.setContent("M416.875,114.441406L405.570312,80.554688C401.265625,67.648438,389.230469,58.976562,375.628906,58.976562L280.617188,58.976562L280.617188,28.042969C280.617188,12.582031,268.046875,0,252.589844,0L165.582031,0C150.128906,0,137.554688,12.582031,137.554688,28.042969L137.554688,58.976562L42.546875,58.976562C28.941406,58.976562,16.90625,67.648438,12.601562,80.554688L1.296875,114.441406C-1.277344,122.15625,0.027344,130.699219,4.78125,137.296875C9.535156,143.894531,17.226562,147.835938,25.359375,147.835938L37.175781,147.835938L63.183594,469.441406C65.117188,493.304688,85.367188,512,109.292969,512L314.15625,512C338.078125,512,358.332031,493.304688,360.261719,469.4375L386.269531,147.835938L392.8125,147.835938C400.945312,147.835938,408.636719,143.894531,413.390625,137.300781C418.144531,130.703125,419.449219,122.15625,416.875,114.441406ZM167.554688,30L250.617188,30L250.617188,58.976562L167.554688,58.976562ZM330.359375,467.019531C329.679688,475.421875,322.5625,482,314.15625,482L109.292969,482C100.886719,482,93.769531,475.421875,93.089844,467.019531L67.273438,147.835938L356.171875,147.835938ZM31.792969,117.835938L41.0625,90.046875C41.273438,89.40625,41.871094,88.976562,42.546875,88.976562L375.628906,88.976562C376.304688,88.976562,376.898438,89.40625,377.113281,90.046875L386.382812,117.835938ZM31.792969,117.835938 " +
        //        "M282.515625,465.957031C282.78125,465.972656,283.042969,465.976562,283.308594,465.976562C291.234375,465.976562,297.859375,459.765625,298.273438,451.757812L312.359375,181.359375C312.789062,173.085938,306.429688,166.027344,298.160156,165.597656C289.867188,165.15625,282.832031,171.523438,282.398438,179.796875L268.316406,450.195312C267.886719,458.46875,274.242188,465.527344,282.515625,465.957031ZM282.515625,465.957031 " +
        //        "M120.566406,451.792969C121.003906,459.789062,127.621094,465.976562,135.53125,465.976562C135.804688,465.976562,136.085938,465.96875,136.363281,465.953125C144.632812,465.503906,150.972656,458.433594,150.523438,450.160156L135.769531,179.761719C135.320312,171.488281,128.25,165.148438,119.976562,165.601562C111.707031,166.050781,105.367188,173.121094,105.816406,181.394531ZM120.566406,451.792969 " +
        //        "M209.253906,465.976562C217.539062,465.976562,224.253906,459.261719,224.253906,450.976562L224.253906,180.578125C224.253906,172.292969,217.539062,165.578125,209.253906,165.578125C200.96875,165.578125,194.253906,172.292969,194.253906,180.578125L194.253906,450.976562C194.253906,459.261719,200.96875,465.976562,209.253906,465.976562ZM209.253906,465.976562");
        //svg.setScaleX(0.05);
        //svg.setScaleY(0.05);
    
        SVGPath svg = new SVGPath();
        svg.setContent("M23,2L17,2L17,0L7,0L7,2L1,2C0.447715,2,0,2.447715,0,3L0,5C0,5.552285,0.447715,6,1,6L23,6C23.552285,6,24,5.552285,24,5L24,3C24,2.447715,23.552285,2,23,2Z " +
                       "M18.28,24L5.82,24C4.806125,24.007948,3.946707,23.255958,3.82,22.25L2,8L22,8L20.27,22.25C20.143745,23.252153,19.290071,24.002871,18.28,24Z");
        svg.getTransforms().add(Transform.affine(0.470658,0,0,0.519992,2.352104,1.774068));
        
        svg.setFill(Color.BLACK);
        //svg.setScaleX(1);
        //svg.setScaleY(1);
        
        Button btn = new Button("del");
        btn.getStyleClass().add("top-button");
        //btn.setGraphic(svg);
        btn.setPrefSize(BUTTON_SIZE.getWidth(), BUTTON_SIZE.getHeight());
        btn.setMaxWidth(BUTTON_SIZE.getWidth());
        btn.setMaxHeight(BUTTON_SIZE.getWidth());
        btn.setOnMouseClicked(event -> {
            graph.clearGraph();
        });
        
        return btn;
    }
    
    //FlatSVGIcon importIcon = new FlatSVGIcon("icons/flatlaf/FileChooserUpFolderIcon.svg").derive(iconSize.width, iconSize.height);
    private Button getImportButton() {
        SVGPath svg = new SVGPath();
        svg.setContent("M2,3L5.5,3L7,5L9,5L9,9L13,9L13,5L14,5L14,13L2,13Z" +
                       "M12,4L12,8L10,8L10,4L8,4L11,1L14,4L12,4Z");
        svg.setScaleX(1.6);
        svg.setScaleY(1.6);
        svg.setFill(Tools.ICON_COLOR);
        
        Button btn = new Button();
        btn.getStyleClass().add("top-button");
        btn.setPrefSize(BUTTON_SIZE.getWidth(), BUTTON_SIZE.getHeight());
        btn.setGraphic(svg);
        
        btn.setOnMouseClicked(event -> {
            if (ImportGraphPane.isOpened) return;
            Parent root = new ImportGraphPane(this);
        });
        
        return btn;
    }
}
