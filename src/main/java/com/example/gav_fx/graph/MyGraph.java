package com.example.gav_fx.graph;

import com.example.gav_fx.core.AlgorithmController;
import com.example.gav_fx.core.GraphChangeObserver;
import com.example.gav_fx.core.GraphObservable;
import com.example.gav_fx.graphbuilder.GraphBuilder;
import com.example.gav_fx.panes.GraphPane;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MyGraph implements GraphObservable {
    
    public int numOfNodes = 0;
    private static int nextId = -1;
    
    public AtomicInteger informedNodes = new AtomicInteger(0);
    
    ListenableGraph<Node, Edge> graph = new DefaultListenableGraph<>(new DefaultUndirectedGraph<>(Edge.class));
    private final Set<GraphChangeObserver> observers = new HashSet<>(5);
    
    // singleton
    private static final MyGraph instance = new MyGraph();
    public static MyGraph getInstance() { return instance; }
    private MyGraph() { setListener(); }
    
    public Graph<Node, Edge> getGraph() { return this.graph; }
    
    // Problematic:
    // gets called when clearing graph, for each node, this is a pretty big problem
    // remove the listener before clearing the graph,
    // then re-add it after the graph is clear!
    private final GraphListener<Node, Edge> GRAPH_LISTENER = new GraphListener<>() {
        @Override public void edgeRemoved(GraphEdgeChangeEvent<Node, Edge> event) {
            observers.forEach(obs -> obs.edgeRemoved(event));
        }
        @Override public void edgeAdded(GraphEdgeChangeEvent<Node, Edge> event) {
            System.out.println(" -> Edge add <-");
            Edge e = event.getEdge();
            // This is needed due to FileGraphImport is is working with default ede constructors
            // (therefore field Line in Edge.class doesn't get set)
            // Problem is in Graph6Sparse6Importer.Consumer.edgeConsumer
            // on line: E e = graph.addEdge(from, to);
            
            // I can't find any way around this, so do some checking here.
            if (e.getLine() == null) {
                e.setLine(event.getEdgeSource(), event.getEdgeTarget());
            }
            observers.forEach(obs -> obs.edgeAdded(event));
        }
        @Override public void vertexAdded(GraphVertexChangeEvent<Node> event) {
            System.out.println(" -> Node add <-");
            observers.forEach(obs -> obs.vertexAdded(event));
        }
        @Override public void vertexRemoved(GraphVertexChangeEvent<Node> event) {
            System.out.println(" -> Node rmv <-");
            observers.forEach(obs -> obs.vertexRemoved(event));
        }
    };
    
    public void setListener() {
        graph.addGraphListener(GRAPH_LISTENER);
    }
    
    public void onGraphImport(GraphBuilder builder) {
        // TODO
        //drawEdges(true);
        System.out.println("NEW GRAPH IMPORT");
        
        clearGraph();
        builder.buildGraph();
        
        //this.graph
        
        this.observers.forEach(GraphChangeObserver::onGraphImport);
    }
    
    public void onGraphClear() {
        this.observers.forEach(GraphChangeObserver::onGraphClear);
    }
    
    public synchronized void clearGraph() {
        nextId = -1;
        numOfNodes = 0;
        
        // remove before clearing graph, re-add after
        //this.graph.removeGraphListener(GRAPH_LISTENER);
        
        // These collections are unmodifiable!
        //  graph.edgeSet().clear();
        //  graph.vertexSet().clear();
        
        // Has its own problems with edge drawing (potentially elsewhere as-well)
        // !!! TODO !!!:
        // Maybe temporarily set edge drawer to null endge drawer, then remove
        // call the line below, then re-add to normal edge drawer?
        // !!! TODO !!!^
//         graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        
        // Does not work (concurrent exception on library's part)
        // this.graph.removeAllVertices(this.graph.vertexSet());
        
        // Slow but works (creates copy references for every edge and node...)
        // (http://jgrapht-users.107614.n3.nabble.com/remove-all-edges-and-vertices-td4024747.html)
        LinkedList<Edge> copy = new LinkedList<>(graph.edgeSet());
        graph.removeAllEdges(copy);
        LinkedList<Node> copy2 = new LinkedList<>(graph.vertexSet());
        graph.removeAllVertices(copy2);
        
        // TODO
        //setListener();
        //onGraphClear();
    }
    
    public void onInformedNodesChange() {
        observers.forEach(GraphChangeObserver::onNewInformedNode);
    }
    
    public synchronized void signalNewInformedNode() {
        this.informedNodes.incrementAndGet();
        onInformedNodesChange();
    }
    
    public void signalNewUninformedNode() {
        this.informedNodes.decrementAndGet();
        onInformedNodesChange();
    }
    
    // TODO: increasing nextId on getNode call, but node is not necessarily added to graph
    @SuppressWarnings("deprecation")
    public static Node getNode() {
        return new Node(-nextId, nextId, ++nextId);
    }
    
    public boolean connectById(int n1Id, int n2Id) {
        Optional<Node> n1 = this.graph.vertexSet().stream().filter(n -> n.id == n1Id).findFirst();
        Optional<Node> n2 = this.graph.vertexSet().stream().filter(n -> n.id == n2Id).findFirst();
        
        if (n1.isPresent() && n2.isPresent()) {
            return addEdge(n1.get(), n2.get());
        } else {
            throw new RuntimeException("Nodes not found by id: " + n1Id + ", " + n2Id + ".");
        }
    }
    
    public synchronized void deleteNode(Node node) {
        // Need to get neighbors before calling removeVertex (due to how Node.getNeighbors is implemented)
        var neighbours = node.getNeighbors();
        
        boolean removed = this.graph.removeVertex(node);
        System.out.println("REMOVING NODE="+node+"="+removed);
        if (removed) numOfNodes--;
        
        // TODO
        //   Maybe there should be a method: Node.onDelete
        if (node.areNeighboursShowing()) {
            // delete its own coords from graph pane
            node.hideNeighboursInfo();
            
            // if his neighbours are showing neighbours
            // then we must update them
            neighbours.stream().filter(Node::areNeighboursShowing).forEach(Node::updateNeighboursInfo);
        }
        if (node.areCoordsShowing()) node.hideCoordsInfo();
        
        // TODO
        // deleting a node, but what state in history should we look at?
        // current? should table stats be changing as we iterate
        // backwards or forwards in history? if yes, then that is not supported currently
        // IF node.isInformed then informedNodes--, and so on...
        
        // delete future history
        graph.vertexSet().forEach(n -> {
            if (n.states.size() > AlgorithmController.totalStates) {
                n.states.subList(AlgorithmController.totalStates, n.states.size()).clear();
            }
        });
        
        // there are no more nodes left
        if (numOfNodes == 0) {
            onGraphClear();
            // TODO
            //drawEdges(false);
        }
    }
    
    public synchronized void addNode(Node n) {
        this.graph.addVertex(n);
        System.out.println("ADDED NODE: " + n);
        numOfNodes++;
        
        n.toFront(); // draw last
    }
    
    // TODO maybe this method should be void
    //  since listeners take care of communication if an edge was added or not?
    public synchronized boolean addEdge(Node n1, Node n2) {
        Edge e = new Edge(n1, n2);
        boolean added = graph.addEdge(n1, n2, e);
        
        n1.updateNeighboursInfo();
        n2.updateNeighboursInfo();
        
        // Maybe don't check this since edges arent added in case of:
        // node1 -----> node2
        // Adding edge : node2 ---> node1 : returns false (since graph is undirected)
        //if (!added) {
            //throw new RuntimeException("Couldn't add edge to graph, edge:" + e + " for nodes: " + n1 + ", " + n2);
        //}
        
        System.out.println("ADDED EDGE: " + e);
        return true;
    }
    
    public Set<Node> getNodes() { return this.graph.vertexSet(); }
    
    public Node getNodeById(int id) {
        Optional<Node> node = this.graph.vertexSet().stream().filter(n -> n.id == id).findFirst();
        return node.orElse(null);
    }
    
    @Override public void addObserver(GraphChangeObserver observer) { this.observers.add(observer); }
    @Override public void removeObserver(GraphChangeObserver observer) { this.observers.remove(observer); }
    
    public void setNumberOfInformedNodes(int num) { this.informedNodes.set(num); this.onInformedNodesChange(); }
    public int getNumberOfInformedNodes() { return this.informedNodes.get(); }
}
