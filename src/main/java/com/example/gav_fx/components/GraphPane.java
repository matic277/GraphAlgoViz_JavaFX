package com.example.gav_fx.components;

import com.example.gav_fx.core.GraphChangeObserver;
import com.example.gav_fx.graph.Edge;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphVertexChangeEvent;

/**
 * PANNING AND ZOOMING
 * taken and adjusted from StackOverflow
 */
public class GraphPane extends Pane implements GraphChangeObserver {
    
    public static DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    
    Group edges;
    Group nodes;
    Group nodeLabels;
    
    public static DoubleProperty OFFSET_X;
    public static DoubleProperty OFFSET_Y;
    
    public static GraphPane INSTANCE;
    
    // Mouse tracking relative to this component
    public static final MouseLocation MOUSE_LOCATION = new MouseLocation();
    public static class MouseLocation  {
        public DoublePropertyBase x = new DoublePropertyBase() {
            @Override public Object getBean() { return GraphPane.MouseLocation.this; }
            @Override public String getName() { return "x"; }
        };
        public DoublePropertyBase y = new DoublePropertyBase() {
            @Override public Object getBean() { return GraphPane.MouseLocation.this; }
            @Override public String getName() { return "y"; }
        };
    }
    
    private Object clickedObjectSource;
    private final ContextMenu edgeMenu = new ContextMenu(); {
        MenuItem item = new MenuItem("Delete");
        item.setOnAction(e -> {
            MyGraph.getInstance().getGraph().removeEdge((Edge)clickedObjectSource);
            clickedObjectSource = null;
            e.consume();
        });
        edgeMenu.getItems().add(item);
    }
    // TODO these menus could probably be in Node/Edge instead of here...
    private final MenuItem idItem = new MenuItem();
    private final MenuItem coordsItem = new MenuItem();
    private final MenuItem neighboursItem = new MenuItem();
    private final MenuItem statesItem = new MenuItem();
    private final MenuItem inform = new MenuItem();
    private final ContextMenu nodeMenu = new ContextMenu(); {
        MenuItem del = new MenuItem("Delete");
        del.setOnAction(e -> {
            MyGraph.getInstance().deleteNode((Node)clickedObjectSource);
            clickedObjectSource = null;
        });
        inform.setOnAction(e -> {
            ((Node)clickedObjectSource).flipInform();
            clickedObjectSource = null;
        });
        idItem.setOnAction(e -> {
            Node n = (Node)clickedObjectSource;
            if (n.isIdShowing()) n.hideIdInfo();
            else n.showIdInfo();
            clickedObjectSource = null;
        });
        coordsItem.setOnAction(e -> {
            Node n = (Node)clickedObjectSource;
            if (n.areCoordsShowing()) n.hideCoordsInfo();
            else n.showCoordsInfo();
            clickedObjectSource = null;
        });
        neighboursItem.setOnAction(e -> {
            Node n = (Node)clickedObjectSource;
            if (n.areNeighboursShowing()) n.hideNeighboursInfo();
            else n.showNeighboursInfo();
            clickedObjectSource = null;
        });
        statesItem.setOnAction(e -> {
            Node n = (Node)clickedObjectSource;
            if (n.areStatesShowing()) n.hideStatesInfo();
            else n.showStatesInfo();
            clickedObjectSource = null;
        });
        nodeMenu.getItems().addAll(del, inform, idItem, coordsItem, neighboursItem, statesItem);
    }
    
    public GraphPane() {
        INSTANCE = this;
        //setPrefSize(600, 600);
        
        this.getStyleClass().add("graph-pane");
        
        // reference/context
        //this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
        
        //this.set
        
        nodes = new Group();
        edges = new Group();
        nodeLabels = new Group();
        
        this.getChildren().add(edges);
        this.getChildren().add(nodes);
        this.getChildren().add(nodeLabels);
        
        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
        
        MyGraph.getInstance().addObserver(this);
        
        OFFSET_X = this.translateXProperty();
        OFFSET_Y = this.translateYProperty();
        
        // TODO
        //  this does not work... node is flashing on hover (ie. gets selected and unselected instantly...)
        //  adding eventHandler instead does not help
        this.setOnMouseClicked(e -> {
            edgeMenu.hide();
            nodeMenu.hide();
            e.consume();
        });
        //this.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
        //    MOUSE_LOCATION.x.set(event.getX());
        //    MOUSE_LOCATION.y.set(event.getY());
        //});
        
        this.setOnMouseMoved(event -> {
            MOUSE_LOCATION.x.set(event.getX());
            MOUSE_LOCATION.y.set(event.getY());
        });
        
        //this.autosize();
    }
    
    public void openContextMenuForEdge(ContextMenuEvent e, Edge selectedEdge) {
        clickedObjectSource = selectedEdge;
        edgeMenu.show(this, e.getScreenX(), e.getScreenY());
    }
    
    public void openContextMenuForNode(ContextMenuEvent e) {
        clickedObjectSource = e.getSource();
        
        Node n = (Node)clickedObjectSource;
        idItem.setText((n.isIdShowing() ? "Hide" : "Show") + " node id");
        coordsItem.setText((n.areCoordsShowing() ? "Hide" : "Show") + " coordinates");
        neighboursItem.setText((n.areNeighboursShowing() ? "Hide" : "Show") + " neighbours");
        statesItem.setText((n.areStatesShowing() ? "Hide" : "Show") + " states");
        inform.setText((n.isInformed() ? "Uninform" : "Inform"));
        
        nodeMenu.show(this, e.getScreenX(), e.getScreenY());
    }
    
    public void addNodeLabel(Label lbl) {
        nodeLabels.getChildren().add(lbl);
    }
    
    public void removeNodeLabel(Label lbl) {
        System.out.println("removing: " + lbl.getText());
        nodeLabels.getChildren().remove(lbl);
    }
    
    @Override
    public void onGraphClear() {
        edges.getChildren().clear();
        nodes.getChildren().clear();
    }
    
    @Override
    public void onGraphImport() {
    
    }
    
    @Override
    public void edgeAdded(GraphEdgeChangeEvent<Node, Edge> event) {
        edges.getChildren().add(event.getEdge().getLine());
    }
    
    @Override
    public void edgeRemoved(GraphEdgeChangeEvent<Node, Edge> event) {
        edges.getChildren().remove(event.getEdge().getLine());
    }
    
    @Override
    public void vertexAdded(GraphVertexChangeEvent<Node> event) { nodes.getChildren().add(event.getVertex()); }
    @Override
    public void vertexRemoved(GraphVertexChangeEvent<Node> event) {
        Node n = event.getVertex();
        nodes.getChildren().remove(n);
        if (n.isIdShowing()) n.hideIdInfo();
        if (n.areCoordsShowing()) n.hideCoordsInfo();
        if (n.areNeighboursShowing()) n.hideNeighboursInfo();
    }
    
    @Override public void onNewInformedNode() {}
    @Override public void onNewUninformedNode() {}
    
    
    
    public double getScale() { return myScale.get(); }
    public void setScale(double scale) {
        //System.out.println("1scale =" + scale);
        //System.out.println("1width =" + this.getWidth());
        //System.out.println("1height=" + this.getHeight());
        myScale.set(scale);
        
        // TODO none of these work (THIS pane stays same size when zooming... not good)
        
        //this.setWidth(this.getWidth()   / scaleXProperty().get());
        //this.setHeight(this.getHeight() / scaleYProperty().get());
        
        //this.setPrefSize(
        //        this.getWidth()/scaleXProperty().get(),
        //        this.getHeight()/scaleYProperty().get());
    
        //this.setWidth(getBoundsInParent().getWidth());
        //this.setHeight(getBoundsInParent().getHeight());
        
        
        //minWidthProperty().divide(myScale);
        
        //System.out.println("2width =" + this.getWidth()  / scaleXProperty().get());
        //System.out.println("2height=" + this.getHeight() / scaleYProperty().get());
    
        //System.out.println("[]width =" + this.getWidth());
        //System.out.println("[]height=" + this.getHeight());
        //System.out.println();
    
        //Random r = new Random();
        //this.setBackground(new Background(new BackgroundFill(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)), CornerRadii.EMPTY, Insets.EMPTY)));
    
    }
    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}

