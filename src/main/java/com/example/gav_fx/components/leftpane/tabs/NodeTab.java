package com.example.gav_fx.components.leftpane.tabs;

import com.example.gav_fx.core.LOG;
import com.example.gav_fx.core.OutputType;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

import static java.lang.Integer.*;

public class NodeTab extends TabContentComponent {
    
    public NodeTab() {
        init();
    }
    
    private void init() {
        VBox searchComponent = getSearchComponent();
        VBox clrComponent = getBorderColorComponent();
        VBox opacityComponent = getOpacityComponent();
        VBox widthComponent = getBorderWidthComponent();
        VBox radiusComponent = getNodeRadiusComponent();
        VBox drawingOptionsComponent = getNodeDrawingOptionsComponent();
        
        this.getChildren().addAll(
                searchComponent,
                drawingOptionsComponent,
                clrComponent,
                opacityComponent,
                widthComponent,
                radiusComponent);
    }
    
    private VBox getSearchComponent() {
        HBox titleContainer = getTitleContainer("Search for node");
        
        Consumer<String> searchFunction = nodeIdText -> {
            try {
                int nodeId = parseInt(nodeIdText);
                MyGraph.getInstance().getNodeById(nodeId).highlight();
            } catch (Exception e) {
                /* Ignore */
                LOG.out("", "Can't find node by id \"" + nodeIdText + "\".", OutputType.ERROR);
            }
        };
    
        TextField input = new TextField();
        input.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.ENTER) return;
            searchFunction.accept(input.getText());
        });
        input.setMaxWidth(100);
        
        Button searchBtn = getSearchIconButton();
        searchBtn.setOnAction(e -> searchFunction.accept(input.getText()));
        
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(5);
        container.setPadding(Insets.EMPTY);
        container.getChildren().addAll(input, searchBtn);
        
        VBox contentContainer = new VBox(container);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private VBox getNodeDrawingOptionsComponent() {
        HBox titleContainer = getTitleContainer("Node drawing options");
    
        CheckBox drawIds = new CheckBox("Draw IDs");
        drawIds.setOnAction(e -> {
            Consumer<Node> f = drawIds.isSelected() ? Node::showIdInfo : Node::hideIdInfo;
            MyGraph.getInstance().getNodes().forEach(f);
        });
        
        CheckBox drawCoords = new CheckBox("Draw coordinates");
        drawCoords.setOnAction(e -> {
            Consumer<Node> f = drawCoords.isSelected() ? Node::showCoordsInfo : Node::hideCoordsInfo;
            MyGraph.getInstance().getNodes().forEach(f);
        });
        
        CheckBox drawNeighbours = new CheckBox("Draw neighbours");
        drawNeighbours.setOnAction(e -> {
            Consumer<Node> f = drawNeighbours.isSelected() ? Node::showNeighboursInfo : Node::hideNeighboursInfo;
            MyGraph.getInstance().getNodes().forEach(f);
        });
    
        VBox contentContainer = new VBox();
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        contentContainer.getChildren().addAll(drawIds, drawCoords, drawNeighbours);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private VBox getNodeRadiusComponent() {
        HBox titleContainer = getTitleContainer("Set node radius");
        
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(100);
        slider.setMin(0);
        slider.setMajorTickUnit(20);
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getNodes().forEach(n -> n.setNewRadius(slider.getValue()));
        });
        
        VBox contentContainer = new VBox(slider);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private VBox getBorderColorComponent() {
        HBox titleContainer = getTitleContainer("Set border color");
        
        ColorPicker clrPicker = new ColorPicker();
        // TODO can all these be binds instead of listening?
        //  same for other eventHandlers in this class
        clrPicker.setOnAction(event -> {
            MyGraph.getInstance().getNodes().forEach(n -> n.setNewBorderColor(clrPicker.getValue()));
        });
        
        VBox contentContainer = new VBox(clrPicker);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        contentContainer.setAlignment(Pos.CENTER);
    
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private VBox getOpacityComponent() {
        HBox titleContainer = getTitleContainer("Set node opacity");
        
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(1);
        slider.setMin(0);
        slider.setValue(1); // default
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getGraph().vertexSet().forEach(n -> n.setNodeOpacity(slider.getValue()));
        });
        
        VBox contentContainer = new VBox(slider);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private VBox getBorderWidthComponent() {
        HBox titleContainer = getTitleContainer("Set border width");
        
        Slider slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setMax(30);
        slider.setMin(0);
        slider.setMajorTickUnit(10);
        slider.setMaxWidth(200);
        slider.valueProperty().addListener(event -> {
            MyGraph.getInstance().getNodes().forEach(n -> n.setNewBorderWidth(slider.getValue()));
        });
        slider.setValue(Node.BORDER_WIDTH); // default
        
        VBox contentContainer = new VBox(slider);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        contentContainer.setAlignment(Pos.CENTER);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    @Override
    public String getTabName() { return "Nodes"; }
}
