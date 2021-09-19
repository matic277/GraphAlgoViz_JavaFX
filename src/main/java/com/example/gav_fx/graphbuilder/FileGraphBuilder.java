package com.example.gav_fx.graphbuilder;

import com.example.gav_fx.graph.Edge;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.graph6.Graph6Sparse6Importer;


public class FileGraphBuilder extends GraphBuilder {
    
    public FileGraphBuilder() {
        super(); // TODO i don't remember these calls being necessary, remove clutter in all implementors
    }
    
    @Override
    public void buildGraph() {
        // graph reading with JGraphT lib
        Graph6Sparse6Importer<Node, Edge> importer = new Graph6Sparse6Importer<>();
        importer.setVertexFactory((t) -> MyGraph.getNode());
        importer.importGraph(MyGraph.getInstance().getGraph(), this.graphFile);
        
        // initialize nodes (informed or not)
        this.getNodeInformator().run();
        this.layoutType.getLayoutExecutor().run();
    }
}
