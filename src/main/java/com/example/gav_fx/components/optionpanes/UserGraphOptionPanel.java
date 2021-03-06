package com.example.gav_fx.components.optionpanes;


import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.graphbuilder.GraphBuilder;
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
