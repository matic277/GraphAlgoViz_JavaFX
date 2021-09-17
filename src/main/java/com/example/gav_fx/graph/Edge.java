package com.example.gav_fx.graph;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jgrapht.graph.DefaultEdge;

import java.util.Collection;
import java.util.Iterator;

public class Edge {
    
    private static final MyGraph graph = MyGraph.getInstance();
    
    Node n1, n2;
    
    // Composition
    Line line; // drawable
    DefaultEdge edge;
    
    public Edge(Node n1, Node n2) {
        //super(n1.getCenterX(), n1.getCenterY(), n2.getCenterX(), n2.getCenterY());
        this.n1 = n1;
        this.n2 = n2;
        this.line = new Line(n1.getCenterX(), n1.getCenterY(), n2.getCenterX(), n2.getCenterY());
        this.edge = graph.graph.addEdge(n1, n2);
        
        this.line.setFill(Color.BLACK);
    }
    
    public Node getN1() { return n1; }
    public Node getN2() { return n2; }
    public Line getLine() { return line; }
    public DefaultEdge getEdge() { return edge; }
    
    public static String edgesListToString(Collection<Node> col) {
        StringBuilder sb = new StringBuilder()
                .append("[");
        if (col.isEmpty()) return sb.append("]").toString();
        
        Iterator<Node> iter = col.iterator();
        for (; iter.hasNext();) {
            Node n = iter.next();
            sb.append(n.id).append(iter.hasNext() ? ", " : "]");
        }
//        sb.append(list.get(list.size()-1).id);
        return sb.append("]").toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if((o == null) || (o.getClass() != this.getClass())) {
            return false;
        }
        Edge e = (Edge) o;
        
        return (n1.id == e.n1.id || n1.id == e.n2.id) &&
                (n2.id == e.n1.id || n2.id == e.n2.id);
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(Math.max(n1.id, n2.id))
                .append(Math.min(n1.id, n2.id))
                .toHashCode();
    }
}
