package com.example.gav_fx.panes.leftpane;

import com.example.gav_fx.panes.leftpane.tabs.LogTab;
import com.example.gav_fx.panes.leftpane.tabs.NodeTab;
import com.example.gav_fx.panes.leftpane.tabs.EdgeTab;
import com.example.gav_fx.panes.leftpane.tabs.SimulationTab;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class LeftPane extends TabPane {
    
    Tab selectedTab;
    
    public LeftPane() {
        this.getStyleClass().add("left-tab-pane");
        
        Tab nodeTab = new Tab();
        nodeTab.setClosable(false);
        NodeTab nTab = new NodeTab();
        nodeTab.setText(nTab.getTabName());
        nodeTab.setContent(nTab);
    
        Tab edgesTab = new Tab();
        edgesTab.setClosable(false);
        EdgeTab eTab = new EdgeTab();
        edgesTab.setText(eTab.getTabName());
        edgesTab.setContent(eTab);
        
        Tab simulationTab = new Tab();
        simulationTab.setClosable(false);
        SimulationTab sTab = new SimulationTab();
        simulationTab.setText(sTab.getTabName());
        simulationTab.setContent(sTab);
    
        Tab logTab = new Tab();
        logTab.setClosable(false);
        LogTab lTab = LogTab.INSTANCE;
        logTab.setText(lTab.getTabName());
        logTab.setContent(lTab);
        
        this.getTabs().addAll(nodeTab, edgesTab, simulationTab, logTab);
        
        
        selectedTab = edgesTab;
        
        this.setSide(Side.LEFT);
        
        //this.setPadding(null);
    }
}
