package com.example.gav_fx.nodeinformator;

import com.example.gav_fx.core.LOG;
import com.example.gav_fx.core.Tools;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;

import java.util.HashSet;
import java.util.Set;

public class NodeInformator implements Runnable {
    
    NodeInformatorProperties properties;
    private int totalInformed; // how many nodes this informator will inform
    
    public NodeInformator(NodeInformatorProperties properties) {
        this.properties = properties;
        this.totalInformed = 0;
    }
    
    public int getNumberOfInformedNodes() { return this.totalInformed; }
    
    @Override
    public void run() {
        MyGraph graph = MyGraph.getInstance();
        
        // If used didn't specify properties then don't do anything?
        if (properties == null) return;
        
        // If user specified:
        //  number of informed nodes = 100
        //  but graph has less than 100 nodes
        //  then this must be corrected !!!
        //  (otherwise this runnable hangs)
        if (properties.getTotalNodesToInform() != null && properties.getTotalNodesToInform() > graph.getNodes().size()) {
            properties.setTotalNodesToInform(Math.min(graph.getNodes().size(), properties.getTotalNodesToInform()));
        }
        
        if (properties.getTotalNodesToInform() != null) {
            int totalNodesToInform = properties.getTotalNodesToInform();
            Set<Integer> alreadyInformed = new HashSet<>(properties.getTotalNodesToInform());
            LOG.out("Informator running based on total nodes to inform: " + totalNodesToInform);
    
            while (totalNodesToInform > 0) {
                int randId = Tools.RAND.nextInt(graph.getNodes().size());
                
                if (alreadyInformed.contains(randId)) continue;
                
                alreadyInformed.add(randId);
                Node n = graph.getNodeById(randId);
                n.getStates().get(0).setState(1); // This always mutates state[0]...
                n.reflectCurrentStateIndex();
                totalNodesToInform--;
            }
            
            // this should be the same... no point in counting in while
            this.totalInformed = properties.getTotalNodesToInform();
        }
        else {
            LOG.out("Informator running based on informed probability: " + properties.getInformedProbability());
            graph.getNodes().forEach(n -> {
                boolean inform = Tools.RAND.nextInt(100) <= properties.getInformedProbability();
                if (inform) this.totalInformed++;
                n.getState().setState(inform ? 1 : 0);
                n.reflectCurrentStateIndex();
            });
        }
    }
}
