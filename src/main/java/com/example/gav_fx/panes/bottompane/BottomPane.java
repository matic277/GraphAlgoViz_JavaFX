package com.example.gav_fx.panes.bottompane;


import com.example.gav_fx.panes.bottompane.tabs.PerformanceTab;
import com.example.gav_fx.panes.bottompane.tabs.StateHistoryTab;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class BottomPane extends TabPane {
    
    StateHistoryTab stateHistoryTab;
    
    public BottomPane() {
        this.setPadding(new Insets(-5));
        this.getStyleClass().add("bottom-tab-pane");
        
        Tab statHistTab = new Tab();
        statHistTab.setClosable(false);
        stateHistoryTab = new StateHistoryTab();
        statHistTab.setText("State history");
        statHistTab.setContent(stateHistoryTab);
        
        Tab perfTab = new Tab();
        perfTab.setClosable(false);
        PerformanceTab pTab = new PerformanceTab();
        perfTab.setText("Performance");
        perfTab.setContent(pTab);
        
        //this.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        
        this.getTabs().addAll(statHistTab, perfTab);
    }
    
    public StateHistoryTab getStateHistoryTab() { return stateHistoryTab; }
}
