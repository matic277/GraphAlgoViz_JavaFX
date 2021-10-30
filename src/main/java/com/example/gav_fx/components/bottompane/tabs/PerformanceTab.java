package com.example.gav_fx.components.bottompane.tabs;

import com.example.gav_fx.components.bottompane.CPUUtilizationUpdater;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class PerformanceTab extends HBox {
    
    private final CPUUtilizationUpdater cpuInfoUpdater;
    
    // These two should match (index-wise) with properties from CPUUtilUpdater
    Label[] cpuUtilInfoText;
    Label[] cpuUtilValueText;
    
    public PerformanceTab() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        
        cpuInfoUpdater = new CPUUtilizationUpdater();
        new Thread(cpuInfoUpdater).start();
        
        IntegerProperty[] properties = cpuInfoUpdater.getProperties();
        cpuUtilInfoText = new Label[properties.length];
        cpuUtilValueText = new Label[properties.length];
        
        for (int i = 0; i < properties.length; i++) {
            Label infoText = new Label(i == properties.length - 1 ? "Total" : "CPU-" + i);
            Label valueText = new Label();
            valueText.textProperty().bind(properties[i].asString());
            
            // Region would be better for styling, but
            // can't bind height property... only add/subract/multiply/...
            Rectangle rect = new Rectangle();
            //rect.getStyleClass().add("cpu-Util-Rect");
            rect.setFill(Color.BLUE);
            rect.heightProperty().bind(properties[i]);
            rect.setWidth(31);
            rect.setArcHeight(10);
            rect.setArcWidth(10);
            
            VBox cpuInfoContainer = new VBox();
            cpuInfoContainer.setPrefHeight(100);
            cpuInfoContainer.setPrefWidth(35);
            cpuInfoContainer.setMinWidth(35);
            cpuInfoContainer.setMaxWidth(35);
            
            cpuInfoContainer.setAlignment(Pos.BOTTOM_LEFT);
            cpuInfoContainer.setPadding(Insets.EMPTY);
            cpuInfoContainer.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1.5))));
            cpuInfoContainer.getChildren().addAll(rect);
            
            VBox mainContainer = new VBox(infoText, cpuInfoContainer);
            mainContainer.setAlignment(Pos.CENTER);
            mainContainer.setSpacing(5);
            mainContainer.setPrefWidth(45);
            
            this.getChildren().add(mainContainer);
        }
    }
}
