package com.example.gav_fx.components.optionpanes;

import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import com.example.gav_fx.nodeinformator.NodeInformatorProperties;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.function.Function;

public class CliqueGraphOptionPanel extends OptionPane {
    
    TextField nodesInput;
    TextField informInput;
    
    public CliqueGraphOptionPanel() {
        init();
    }
    
    private void init() {
        VBox container = new VBox();
        VBox nodeItems = getNodesInputContainer();
        VBox informItems = getEdgeProbabilityInputContainer();
        
        container.getChildren().add(nodeItems);
        container.getChildren().add(informItems);
        
        nodesInput = (TextField) nodeItems.getChildren().get(1);
        informInput = (TextField) informItems.getChildren().get(1);
        
        this.getChildren().add(container);
    }
    
    @Override
    public Function<ImportType, GraphBuilder> getBuilder() {
        return importType -> {
            int nodesToInform = -1; // Not ideal
            double informProb = -1;
            try { nodesToInform = Integer.parseInt(informInput.getText()); }
            catch (Exception e) { System.out.println("error parsing int value " + informInput.getText()); }
            
            if (nodesToInform == -1) {
                try { informProb = Double.parseDouble(informInput.getText()); }
                catch (Exception e) { System.out.println("error parsing double value " + informInput.getText()); }
            }
            
            // TODO
            // ugly, all of it
            if (nodesToInform == -1 && informProb == -1) return null; // signal bad input, not return null!
            
            NodeInformatorProperties informatorProperties = new NodeInformatorProperties();
            if (informProb == -1) informatorProperties.setTotalNodesToInform(nodesToInform);
            else                  informatorProperties.setInformedProbability(informProb);
            
            return importType.getGraphBuilder()
                    .setNumberOfNodes(Integer.parseInt(nodesInput.getText()))
                    .setInformatorProperties(informatorProperties);
        };
    }
}
