package com.example.gav_fx.panes;

import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.core.LayoutType;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ImportGraphPane extends BorderPane {
    
    Pane optionPane;
    ImportType currentSelectedInputType;
    
    public ImportGraphPane() {
        Stage stage = new Stage();
        stage.setTitle("Import new graph window");
        stage.setScene(new Scene(this, 450, 450));
        stage.show();
    
        currentSelectedInputType = ImportType.STATIC_TEST;
        optionPane = currentSelectedInputType.getPanel();
        
        initTop();
        initLeftSize();
        initMiddle();
        initRightSize();
        initBottom();
    }
    
    private void initLeftSize() {
        BorderPane container = new BorderPane();
    
        Label title = new Label("Select import");
        ListView<ImportType> list = new ListView<>(FXCollections.observableArrayList(ImportType.values()));
        
        container.setTop(title);
        container.setCenter(list);
        
        this.setLeft(container);
    }
    
    private void initMiddle() {
        this.setCenter(optionPane);
    }
    
    private void initRightSize() {
        BorderPane container = new BorderPane();
        
        Label title = new Label("Select layout");
        ListView<LayoutType> list = new ListView<>(FXCollections.observableArrayList(LayoutType.values()));
    
        container.setTop(title);
        container.setCenter(list);
        
        this.setRight(container);
    }
    
    private void initBottom() {
    }
    
    private void initTop() {
    
    }
}
