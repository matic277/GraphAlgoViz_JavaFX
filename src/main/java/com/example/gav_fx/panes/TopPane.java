package com.example.gav_fx.panes;

import com.example.gav_fx.graph.MyGraph;
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
        
        initImportButton();
        initDeleteGraphButton();
        initAddNodeButton();
    }
    
    private void initAddNodeButton() {
        Button btn = new Button("add node");
        btn.setOnMouseClicked((event) -> {
            //graph.addNode();
        });
        
        this.getChildren().add(btn);
    }
    
    private void initDeleteGraphButton() {
        Button btn = new Button("delete");
        btn.setOnMouseClicked((event) -> {

        });
        
        this.getChildren().add(btn);
    }
    
    //FlatSVGIcon importIcon = new FlatSVGIcon("icons/flatlaf/FileChooserUpFolderIcon.svg").derive(iconSize.width, iconSize.height);
    private void initImportButton() {
        Button btn = new Button("import");
        //btn.getStyleClass().add("icon-button");
        btn.setOnMouseClicked((event) -> {
            
            Parent root = new ImportGraphPane();
            
        });
        
        this.getChildren().add(btn);
    }
}
