package com.example.gav_fx.components.optionpanes;

import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import javafx.scene.control.Label;

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