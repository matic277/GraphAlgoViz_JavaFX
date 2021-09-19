package com.example.gav_fx.panes.optionpanes;

import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import com.example.gav_fx.nodeinformator.NodeInformatorProperties;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.function.Function;

public class FileGraphOptionPanel extends OptionPane {
    
    TextField pathToGraph;
    
    public FileGraphOptionPanel() {
        init();
    }
    
    private void init() {
        VBox container = new VBox();
        VBox items = getGenericInputContainer("Path to graph file");
        container.getChildren().add(items);
        
        pathToGraph = (TextField) items.getChildren().get(1);
        pathToGraph.setText("./graphs/graph1.g6");
        
        this.getChildren().add(container);
    }
    
    public Function<ImportType, GraphBuilder> getBuilder() {
        return importType -> {
            File graphFile = new File(pathToGraph.getText());
            if (!(graphFile.canRead() && graphFile.isFile())) {
                // TODO signal bad imput
                return null;
            }
            return importType.getGraphBuilder()
                    .setGraphSourceFile(graphFile);
        };
    }
}
