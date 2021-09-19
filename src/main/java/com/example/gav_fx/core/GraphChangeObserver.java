package com.example.gav_fx.core;

import com.example.gav_fx.graph.Edge;
import com.example.gav_fx.graph.Node;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.graph.DefaultEdge;

public interface GraphChangeObserver extends GraphListener<Node, Edge> {
    
    // Inherited from GraphListener<>:
    //   edgeRemoved
    //   edgeAdded
    //   vertexAdded
    //   vertexRemoved
    
    void onGraphClear();
    void onGraphImport();
    
    void onNewInformedNode();
    void onNewUninformedNode();
}
