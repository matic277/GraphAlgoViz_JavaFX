package com.example.gav_fx.components.bottompane.tabs;

import com.example.gav_fx.components.BoxUIComponent;
import com.example.gav_fx.core.*;
import com.example.gav_fx.graph.Edge;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphVertexChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class StateHistoryTab extends Pane implements StateObserver, GraphChangeObserver {
    
    WorkerController workerController;
    
    FlowPane contentContainer;
    final List<RoundStatisticsComponent> components = new ArrayList<>(16);
    
    public StateHistoryTab() {
        MyGraph.getInstance().addObserver(this);
        this.setPrefHeight(200);
        
        contentContainer = new FlowPane();
        contentContainer.setPadding(new Insets(10, 10, 10, 15));
        contentContainer.setHgap(10);
        contentContainer.setVgap(5);                                           // insets
        contentContainer.prefWrapLengthProperty().bind(this.widthProperty().subtract(20));
        //buttonsContainer.prefWidthProperty().bind(this.widthProperty());
    
        initOnGraphImport();
        
        this.getChildren().add(contentContainer);
        
        //this.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        //buttonsContainer.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    public void initOnGraphImport() {
        RoundStatisticsComponent.highlightedComponent = new RoundStatisticsComponent(new RoundStatisticsData(0, 0,0));
        RoundStatisticsComponent.highlightedComponent.getUIComponent().setEffect(Tools.SHADOW_EFFECT_COMPONENT);
        components.add(RoundStatisticsComponent.highlightedComponent);
        contentContainer.getChildren().add(RoundStatisticsComponent.highlightedComponent.getUIComponent());
    }
    
    public void setWorkerController(WorkerController wc) { workerController = wc; }
    
    @Override
    public void onStateChange() {
        // TODO not working correctly
        System.out.println("STATE CHANGE");
        int index = WorkerController.currentStateIndex;
        components.get(index).highlight();
    }
    
    @Override
    public void onNewState(RoundStatisticsData roundStats) {
        // TODO not working correctly
        System.out.println("NEW STATE");
        
        RoundStatisticsComponent newComponent = new RoundStatisticsComponent(roundStats);
        components.add(newComponent);
        newComponent.highlight();
        Platform.runLater(() -> contentContainer.getChildren().add(newComponent.getUIComponent()));
    }
    
    @Override
    public void onGraphClear() {
        contentContainer.getChildren().clear();
        components.clear();
        RoundStatisticsComponent.highlightedComponent = null;
    }
    
    @Override
    public void onGraphImport() {
        initOnGraphImport();
    }
    
    @Override public void onNewInformedNode() {}
    @Override public void onNewUninformedNode() {}
    @Override public void edgeAdded(GraphEdgeChangeEvent<Node, Edge> e) {}
    @Override public void edgeRemoved(GraphEdgeChangeEvent<Node, Edge> e) {}
    @Override public void vertexAdded(GraphVertexChangeEvent<Node> e) {}
    @Override public void vertexRemoved(GraphVertexChangeEvent<Node> e) {}
    
    public class RoundStatisticsComponent {
        private static RoundStatisticsComponent highlightedComponent; // indicating current state index
        private static final Dimension2D BUTTON_SIZE = new Dimension2D(40, 30);
        private static final int paddedStrLen = 20;
        private final RoundStatisticsData statsData;
        private final BoxUIComponent uiComponent;
        private final Label timeInfo;
        private final Label threadInfo;
        
        public RoundStatisticsComponent(RoundStatisticsData statsData) {
            this.statsData = statsData;
            timeInfo = new Label(StringUtils.rightPad("Time elapsed: " + statsData.getTotalTimeElapsed(), paddedStrLen));
            threadInfo = new Label(StringUtils.rightPad("Threads used: " + statsData.getThreadCount(), paddedStrLen));

            //this.getStyleClass().add("round-stats-component");
            uiComponent = new BoxUIComponent();
            uiComponent.getTitleLabel().setText(statsData.getRoundNumber() + "");
            uiComponent.setOnMouseClicked(event -> {
                System.out.println("SETTING TO " + statsData.getRoundNumber());
                StateHistoryTab.this.workerController.setCurrentStateToIndex(statsData.getRoundNumber());
                highlight();
            });
            
            VBox content = new VBox(
                timeInfo,
                threadInfo);
            content.setPadding(new Insets(5));
            uiComponent.getMainContainer().getChildren().add(content);
        }
    
        public void highlight() {
            if (highlightedComponent == null) {
                LOG.error("HighlightedComponent is null!");
                return;
            }
            highlightedComponent.getUIComponent().setEffect(null);
            highlightedComponent = this;
            uiComponent.setEffect(Tools.SHADOW_EFFECT_COMPONENT);
        }
        
        public Pane getUIComponent() {
            return uiComponent;
        }
    }
}
