package com.example.gav_fx.panes.leftpane.tabs;

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
import javafx.scene.shape.SVGPath;

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
        
        // Source: https://commons.wikimedia.org/wiki/File:VisualEditor_-_Icon_-_Search.svg
        SVGPath svgIcon = new SVGPath();
        svgIcon.setContent("M16.021,15.96l-2.374-2.375c-0.048-0.047-0.105-0.079-0.169-0.099c0.403-0.566,0.643-1.26,0.643-2.009 " +
                "C14.12,9.557,12.563,8,10.644,8c-1.921,0-3.478,1.557-3.478,3.478c0,1.92,1.557,3.477,3.478,3.477c0.749,0,1.442-0.239,2.01-0.643 " +
                "c0.019,0.063,0.051,0.121,0.098,0.169l2.375,2.374c0.19,0.189,0.543,0.143,0.79-0.104S16.21,16.15,16.021,15.96z M10.644,13.69 " +
                "c-1.221,0-2.213-0.991-2.213-2.213c0-1.221,0.992-2.213,2.213-2.213c1.222,0,2.213,0.992,2.213,2.213 " +
                "C12.856,12.699,11.865,13.69,10.644,13.69z");
        svgIcon.setScaleX(1.3);
        svgIcon.setScaleY(1.3);
        
        Button searchBtn = new Button();
        searchBtn.setGraphic(svgIcon);
        searchBtn.setPrefWidth(25);
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
