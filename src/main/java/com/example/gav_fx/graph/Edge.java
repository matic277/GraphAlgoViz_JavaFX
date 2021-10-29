package com.example.gav_fx.graph;

import com.example.gav_fx.core.Tools;
import com.example.gav_fx.components.GraphPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.jgrapht.graph.DefaultEdge;

public class Edge extends DefaultEdge {
    
    private static final MyGraph graph = MyGraph.getInstance();
    
    // TODO for easier selecting when edge is thin
    //  but when zooming in, thickness should be adjusted, otherwise even
    //  thin edges would be overlapping... but how performant is this
    //  when graph is dense and lare (ie lots of edges)...
    public static int BORDER_WIDTH = 3;
    
    Line line; // drawable
    
    private Color color = Color.BLACK; // default
    public static double strokeWidth = 1;
    
    private static Edge HIGHLIGHTED_EDGE = null; // only one highlighted node at a time for now...
    
    // Note:
    // This is a constructor for JGraphT and is only needed when calling
    //  graph.addEdge(v1, v2).
    // If calling graph.addEdge(v1, v2, edge), then it's not needed.
    public Edge() { }
    
    public Edge(Node n1, Node n2) {
        super();
        
        //this.n2 = n2;
        line = new Line(n1.getCenterX(), n1.getCenterY(), n2.getCenterX(), n2.getCenterY());
        bindEdgeToNodes(n1, n2, line);
        
        line.setStroke(color); // default
        line.setStrokeWidth(strokeWidth);
        
        // Highlight on mouse hover
        line.setOnMouseEntered(e -> {
            setEdgeColor(color.invert());
        });
        line.setOnMouseExited(e -> {
            setEdgeColor(color.invert());
        });
        
        // Right-click menu for deleting edges
        line.setOnContextMenuRequested(e -> GraphPane.INSTANCE.openContextMenuForEdge(e, this));
    }
    
    //public void setParentComponent(GraphPane parent) { this.parent = parent; }
    
    public Line getLine() { return line; }
    
    public void setLine(Node n1, Node n2) {
        this.line = new Line(n1.getCenterX(), n1.getCenterY(), n2.getCenterX(), n2.getCenterY());
        bindEdgeToNodes(n1, n2, line);
    }
    
    // TODO implementing draggable nodes:
    //  https://stackoverflow.com/questions/49951275/binding-line-with-circles-coordinate-doesnt-work
    // Binding coords of line(edge) to nodes
    private static void bindEdgeToNodes(Node n1, Node n2, Line l) {
        l.startXProperty().bind(n1.centerXProperty());
        l.startYProperty().bind(n1.centerYProperty());
        
        l.endXProperty().bind(n2.centerXProperty());
        l.endYProperty().bind(n2.centerYProperty());
    }
    
    public void setEdgeWidth(double newWidth) {
        strokeWidth = newWidth;
        line.setStrokeWidth(strokeWidth);
    }
    
    public void setEdgeColor(Color newColor) {
        color = newColor;
        this.line.setStroke(color);
    }
    
    public void setEdgeOpacity(double opacity) {
        Color clr = (Color) this.line.getStroke();
        color = new Color(
                clr.getRed(),
                clr.getGreen(),
                clr.getBlue(),
                opacity);
        line.setStroke(color);
    }
    
    public void highlight() {
        if (HIGHLIGHTED_EDGE != null) HIGHLIGHTED_EDGE.getLine().setEffect(null);
        HIGHLIGHTED_EDGE = this;
        HIGHLIGHTED_EDGE.getLine().setEffect(Tools.SHADOW_EFFECT_SEARCH);
    }

//    public static String edgesListToString(Collection<Node> col) {
//        StringBuilder sb = new StringBuilder()
//                .append("[");
//        if (col.isEmpty()) return sb.append("]").toString();
//
//        Iterator<Node> iter = col.iterator();
//        for (; iter.hasNext();) {
//            Node n = iter.next();
//            sb.append(n.id).append(iter.hasNext() ? ", " : "]");
//        }
////        sb.append(list.get(list.size()-1).id);
//        return sb.append("]").toString();
//    }
    
    //@Override
    //public boolean equals(Object o) {
    //    if((o == null) || (o.getClass() != this.getClass())) {
    //        return false;
    //    }
    //    Edge e = (Edge) o;
    //
    //    return (n1.id == e.n1.id || n1.id == e.n2.id) &&
    //            (n2.id == e.n1.id || n2.id == e.n2.id);
    //}
    //
    //@Override
    //public int hashCode() {
    //    return new HashCodeBuilder()
    //            .append(Math.max(n1.id, n2.id))
    //            .append(Math.min(n1.id, n2.id))
    //            .toHashCode();
    //}
}
