package com.example.gav_fx.components.bottompane.tabs;

import com.example.gav_fx.components.bottompane.CPUUtilizationUpdater;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class PerformanceTab extends HBox {
    
    private static PerformanceTab INSTANCE;
    
    private final CPUUtilizationUpdater cpuInfoUpdater;
    
    // These two should match (index-wise) with properties from CPUUtilUpdater
    Label[] cpuUtilInfoText;
    Label[] cpuUtilValueText;
    
    public PerformanceTab() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        INSTANCE = this;
        
        cpuInfoUpdater = new CPUUtilizationUpdater();
        new Thread(cpuInfoUpdater).start();
        
        IntegerProperty[] properties = cpuInfoUpdater.getProperties();
        cpuUtilInfoText = new Label[properties.length];
        cpuUtilValueText = new Label[properties.length];
        
        for (int i = 0; i < properties.length; i++) {
            Label infoText = new Label(i == properties.length - 1 ? "Total" : "CPU-" + i);
            Label valueText = new Label();
            valueText.textProperty().bind(properties[i].asString());
            
            VBox cpuInfoContainer = new VBox();
            cpuInfoContainer.setMaxHeight(80);
            cpuInfoContainer.setPadding(new Insets(5));
            cpuInfoContainer.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
            cpuInfoContainer.getChildren().addAll(infoText, valueText);
            this.getChildren().add(cpuInfoContainer);
        }
    }
}
