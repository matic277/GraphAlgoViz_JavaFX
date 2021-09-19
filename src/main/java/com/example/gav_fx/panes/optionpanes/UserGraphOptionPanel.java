package com.example.gav_fx.panes.optionpanes;


import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.graphbuilder.EmptyGraphBuilder;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

import java.util.function.Function;

public class UserGraphOptionPanel extends OptionPane {
    
    public UserGraphOptionPanel() {
        init();
    }
    
    private void init() {
        Label info = new Label("Make your own graph");
        this.getChildren().add(info);
    }
    
    @Override
    public Function<ImportType, GraphBuilder> getBuilder() {
        // Nothing to do
        return ImportType::getGraphBuilder;
    }
}
