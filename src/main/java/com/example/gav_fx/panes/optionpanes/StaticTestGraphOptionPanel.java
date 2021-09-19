package com.example.gav_fx.panes.optionpanes;

import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

import java.io.File;
import java.util.function.Function;

public class StaticTestGraphOptionPanel extends OptionPane {
    
    public StaticTestGraphOptionPanel() {
        Label l = new Label("STATIC IMPORT TEST");
        this.getChildren().add(l);
    }
    
    @Override
    public Function<ImportType, GraphBuilder> getBuilder() {
        // Nothing to do
        return ImportType::getGraphBuilder;
    }
}