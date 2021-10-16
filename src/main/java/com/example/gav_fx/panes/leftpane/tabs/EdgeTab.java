package com.example.gav_fx.panes.leftpane.tabs;

import com.example.gav_fx.core.Tools;
import com.example.gav_fx.core.Tools.Tuple;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.function.Supplier;

public class EdgeTab extends TabElement {
    
    MyGraph graph = MyGraph.getInstance();
    
    Supplier<Label> infoLabelRef;
    Supplier<Thread> threadSupplier = () -> new Thread(() -> { Tools.sleep(3000); Platform.runLater(() -> infoLabelRef.get().setText(" ")); });
    // TODO This is not ideal. Multiple consecutive clicks start multiple threads, which arent synched, and so on... i'm too lazy to fix this atm
    
    public EdgeTab() {
        init();
    }
    
    private void init() {
        VBox widthComponent = getEdgeWidthComponent();
        VBox colorComponent = getEdgeColorComponent();
        VBox opacityComponent = getOpacityComponent();
        VBox addRemoveEdgeComponent = getAddRemoveEdgeComponent();
        
        this.getChildren().addAll(
                widthComponent,
                colorComponent,
                opacityComponent,
                addRemoveEdgeComponent);
    }
    
    private VBox getAddRemoveEdgeComponent() {
        final int maxWidth = 215;
        final int maxInputField = 100; // TODO unnecessary
        HBox titleContainer = getTitleContainer("Add or remove edge");
        
        Label n1Text = new Label("Node 1 ID:");
        TextField n1Input = new TextField();
        n1Input.setMaxWidth(maxInputField);
        VBox n1Container = new VBox(n1Text, n1Input);
        //n1Container.setSpacing(3);
        
        TextField n2Input = new TextField();
        n2Input.setMaxWidth(maxInputField);
        Label n2Text = new Label("Node 2 ID:");
        VBox n2Container = new VBox(n2Text, n2Input);
        //n1Container.setSpacing(3);
        
        HBox nContainer = new HBox(n1Container, n2Container);
        nContainer.setSpacing(5);
        
        Label info = new Label("<info>");
        infoLabelRef = () -> info;
        
        Button addBtn = new Button("Add");
        addBtn.setOnMouseClicked(e -> {
            info.setText("Creating edge...");
            // TODO locking... cant be removing while adding edges and vice-versa, or can we?
            
            Tuple<Node> nodes = getNodesById(n1Input.getText(), n2Input.getText(), info);
            if (nodes == null) return;
            
            boolean added = graph.addEdge(nodes.getLeft(), nodes.getRight());
            info.setText("Edge was " + (added ? "" : "not") + " added");
            threadSupplier.get().start();
        });
        Button rmvBtn = new Button("Remove");
        rmvBtn.setOnMouseClicked(e -> {
            // TODO
        });
        HBox btnContainer = new HBox(addBtn, rmvBtn);
        btnContainer.setSpacing(5);
        btnContainer.setAlignment(Pos.BASELINE_RIGHT);
        
        VBox contentContainer = new VBox(titleContainer, nContainer, info, btnContainer);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        
        VBox mainContainer = getMainContainer(titleContainer, contentContainer);
        return mainContainer;
    }
    
    private VBox getOpacityComponent() {
        HBox titleContainer = getTitleContainer("Set edge opacity");
        
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(1);
        slider.setMin(0);
        slider.setValue(1); // default
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getGraph().edgeSet().forEach(e -> e.setEdgeOpacity(slider.getValue()));
        });
    
        VBox contentContainer = new VBox(slider);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private VBox getEdgeWidthComponent() {
        HBox titleContainer = getTitleContainer("Set edge width");
        
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(30);
        slider.setMin(0);
        slider.setMajorTickUnit(10);
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getGraph().edgeSet().forEach(e -> e.setEdgeWidth(slider.getValue()));
        });
        
        VBox contentContainer = new VBox(slider);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private VBox getEdgeColorComponent() {
        HBox titleContainer = getTitleContainer("Set edge color");
        
        ColorPicker clrPicker = new ColorPicker();
        clrPicker.setOnAction(event -> {
            MyGraph.getInstance().getGraph().edgeSet().forEach(e -> e.setEdgeColor(clrPicker.getValue()));
        });
        
        VBox contentContainer = new VBox(clrPicker);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        contentContainer.setAlignment(Pos.CENTER);
    
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private Tuple<Node> getNodesById(String id1Str, String id2Str, Label info) {
        id1Str = id1Str.trim();
        id2Str = id2Str.trim();
        
        int id1 = 0, id2 = 0;
        try { id1 = Integer.parseInt(id1Str); id2 = Integer.parseInt(id2Str); }
        catch (Exception ex) { info.setText("Input must be a number!"); threadSupplier.get().start(); return null; }
        
        Node n1 = graph.getNodeById(id1), n2 = graph.getNodeById(id2);
        if (n1 == null || n2 == null) {
            info.setText("Can't find node by id " + (n1 == null ? id1 : id2));
            threadSupplier.get().start();
            return null;
        }
        return new Tuple<>(n1, n2);
    }
    
    @Override
    public String getTabName() { return "Edges"; }
}