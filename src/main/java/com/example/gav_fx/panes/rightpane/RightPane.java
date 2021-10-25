package com.example.gav_fx.panes.rightpane;

import com.example.gav_fx.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.stream.Collectors;


public class RightPane extends Pane {
    
    private final Label mouseLocationIndicator;
    private final VBox contentContainer;
    private final ObservableList<TableDataRow> tableRows = FXCollections.observableList(
            Arrays.stream(TableDataType.values()).map(TableDataRow::new).collect(Collectors.toList())
    );
    
    
    public RightPane() {
        this.setMinWidth(100);
        this.getStyleClass().add("right-pane");
        
        mouseLocationIndicator = new Label();
        contentContainer = new VBox();
        contentContainer.setPadding(new Insets(10, 10, 10, 10));
        contentContainer.getChildren().add(mouseLocationIndicator);
        
        TableView<TableDataRow> table = new TableView<>();
        TableColumn col1 = new TableColumn("type");
        col1.setCellValueFactory(new PropertyValueFactory<TableDataRow, String>("type"));
        TableColumn col2 = new TableColumn("value");
        col2.setCellValueFactory(new PropertyValueFactory<TableDataRow, String>("value"));
        table.getColumns().addAll(col1, col2);
        table.setItems(tableRows);
        contentContainer.getChildren().add(table);
        
        table.setPrefHeight(100);
        
        
        this.getChildren().add(contentContainer);
    }
    
    public void updateMousePosition() {
        mouseLocationIndicator.setText(
                "(" +
                (int) App.MOUSE_LOCATION.x.get() + ", " +
                (int) App.MOUSE_LOCATION.y.get() +
                ")");
    }
}
