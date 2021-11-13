package com.example.gav_fx.components.optionpanes;

import com.example.gav_fx.core.ImportType;
import com.example.gav_fx.core.LOG;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import com.example.gav_fx.nodeinformator.NodeInformatorProperties;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.function.Function;

public class RandomGraphOptionPanel extends OptionPane {
    
    TextField nodesInput;
    TextField edgeInput;
    TextField informInput;
    
    public RandomGraphOptionPanel() {
        //Label l = new Label("RANDOM");
        //this.getChildren().add(l);
        
        init();
    }
    
    private void init() {
        VBox container = new VBox();
        VBox nodesItems = getNodesInputContainer();
        VBox edgeItems = getEdgeProbabilityInputContainer();
        VBox informItems = getInformedProbabilityContainer();
        
        nodesInput = (TextField) nodesItems.getChildren().get(1);
        edgeInput  = (TextField) edgeItems.getChildren().get(1);
        informInput = (TextField) informItems.getChildren().get(1);
        
        container.getChildren().add(nodesItems);
        container.getChildren().add(edgeItems);
        container.getChildren().add(informItems);
        this.getChildren().add(container);
    }
    
    @Override
    public Function<ImportType, GraphBuilder> getBuilder() {
        return importType -> {
            int nodesToInform = -1; // Not ideal
            double informProb = -1;
            
            String textToParse = informInput.getText().trim();
            
            if (textToParse.endsWith("%"))
            try { nodesToInform = Integer.parseInt(textToParse); }
            catch (Exception e) { /* do nothing */  }
    
            textToParse = textToParse.replace("%", "");
            if (nodesToInform == -1) {
                try { informProb = Double.parseDouble(textToParse); }
                catch (Exception e) { LOG.error("error parsing value " + textToParse); }
            }
            
            // TODO
            //  ugly, all of it
            if (nodesToInform == -1 && informProb == -1) return null;
            
            NodeInformatorProperties informatorProperties = new NodeInformatorProperties();
            if (informProb == -1) informatorProperties.setTotalNodesToInform(nodesToInform);
            else                  informatorProperties.setInformedProbability(informProb);
            
            return importType.getGraphBuilder()
                    .setEdgeProbability(Double.parseDouble(edgeInput.getText()))
                    .setNumberOfNodes(Integer.parseInt(nodesInput.getText()))
                    .setInformatorProperties(informatorProperties);
        };
    }
}
