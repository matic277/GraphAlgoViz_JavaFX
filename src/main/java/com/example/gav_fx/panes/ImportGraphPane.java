package com.example.gav_fx.panes;

import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.core.LayoutType;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import com.example.gav_fx.panes.optionpanes.OptionPane;
import com.example.gav_fx.panes.toppane.TopPane;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ImportGraphPane extends BorderPane {
    
    TopPane parent;
    Stage stage;
    
    public static boolean isOpened = false;
    
    Pane optionPaneParent;
    OptionPane optionPane;
    
    ImportType selectedImportType;
    LayoutType selectedLayoutType;
    
    public ImportGraphPane(TopPane parent) {
        this.parent = parent;
        this.optionPaneParent = new Pane();
        
        selectedImportType = ImportType.STATIC_TEST; // defaults
        selectedLayoutType = LayoutType.RANDOM;
        optionPane = selectedImportType.getOptionPane();
        selectedLayoutType = null;
        
        initTop();
        initLeftSize();
        initMiddle();
        //initRightSize();
        initBottom();
        
        stage = new Stage();
        stage.setTitle("Import new graph window");
        stage.setScene(new Scene(this, 450, 450));
        stage.setOnCloseRequest(event -> isOpened = false);
        stage.close();
        //stage.sizeToScene();
        stage.show();
        
        isOpened = true;
    }
    
    private void initLeftSize() {
        // new FlowPane(Orientation.VERTICAL); just doesn't work
        VBox importTypeContainer = new VBox();
        Label importTitle = new Label("Select import");
        ListView<ImportType> importList = new ListView<>(FXCollections.observableArrayList(ImportType.values()));
        importList.getSelectionModel().select(selectedImportType); // selected by default
        importList.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            selectedImportType = newVal;
            setOptionPane(selectedImportType);
        });
        
        importTypeContainer.getChildren().add(importTitle);
        importTypeContainer.getChildren().add(importList);
        
        VBox layoutTypeContainer = new VBox(5);
        Label layoutTitle = new Label("Select Layout");
        ListView<LayoutType> layoutList = new ListView<>(FXCollections.observableArrayList(LayoutType.values()));
        layoutList.getSelectionModel().select(selectedLayoutType); // selected by default
        layoutList.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            selectedLayoutType = newVal;
        });
        
        layoutTypeContainer.getChildren().add(layoutTitle);
        layoutTypeContainer.getChildren().add(layoutList);
        
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(10, 10, 10, 10));
        //mainContainer.getChildren().add(topSpacer);
        mainContainer.getChildren().add(importTypeContainer);
        mainContainer.getChildren().add(layoutTypeContainer);
        
        // Listview for some reason makes 17 rows
        importList.setPrefHeight(importList.getItems().size() * 24); // This shit sucks
        layoutList.setPrefHeight(layoutList.getItems().size() * 24);
        
        this.setLeft(mainContainer);
    }
    
    private void initMiddle() {
        Label title = new Label("Required parameters:");
        
        setOptionPane(selectedImportType);
        
        VBox container = new VBox();
        container.setPadding(new Insets(10, 10, 10, 10));
        container.getChildren().add(title);
        container.getChildren().add(optionPaneParent);
        
        // Border
        // (one or the other)
        //container.setBorder(new Border(new BorderStroke(Color.CHOCOLATE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //container.setStyle("-fx-padding: 10;" +
        //        "-fx-border-style: solid inside;" +
        //        "-fx-border-width: 2;" +
        //        "-fx-border-insets: 5;" +
        //        "-fx-border-radius: 5;" +
        //        "-fx-border-color: blue;");
        
        
        this.setCenter(container);
    }
    
    private void setOptionPane(ImportType type) {
        optionPane = type.getOptionPane();
        
        optionPaneParent.getChildren().clear();         // remove old
        optionPaneParent.getChildren().add(optionPane); // add new
    }
    
    private void initBottom() {
        Button btn = new Button("import");
        
        // TODO:
        //  building graph and doing layout should probably be done
        //  in another thread, while there is a spinner in middle screen,
        //  implying the graph is loading...
        btn.setOnMouseClicked(event -> {
            GraphBuilder builder = optionPane.getBuilder().apply(selectedImportType);
            builder.setLayoutType(selectedLayoutType);
            
            ImportGraphPane.isOpened = false;
            stage.close();
            MyGraph.getInstance().onGraphImport(builder);
        });
        FlowPane container = new FlowPane();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10, 10, 10, 10));
        container.getChildren().add(btn);
        
        this.setBottom(container);
    }
    
    private void initTop() {
    
    }
}
