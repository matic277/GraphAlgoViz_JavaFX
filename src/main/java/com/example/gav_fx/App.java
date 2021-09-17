package com.example.gav_fx;

import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import com.example.gav_fx.panes.BottomPane;
import com.example.gav_fx.panes.GraphPane;
import com.example.gav_fx.panes.TopPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public class App extends Application {
    
    @Override
    public void start(Stage stage) {
        // Container of GraphPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("edge-to-edge"); // removing blue border/highlight when clicked/panning
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        AnchorPane.setTopAnchor(scrollPane, 10.0d);
        AnchorPane.setRightAnchor(scrollPane, 10.0d);
        AnchorPane.setBottomAnchor(scrollPane, 10.0d);
        AnchorPane.setLeftAnchor(scrollPane, 10.0d);
    
        SVGPath x = new SVGPath();
        
        // TOP
        TopPane topPane = new TopPane();
        // MIDDLE
        GraphPane graphPane = new GraphPane(scrollPane);
        buildGraph();
        graphPane.getChildren().addAll(MyGraph.getInstance().getNodes());
        // BOTTOM
        BottomPane bottomPane = new BottomPane();
        
        // MAIN CONTAINER
        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        root.setBottom(bottomPane);
        root.setTop(topPane);
        
        Scene scene = new Scene(root, 1000, 1000);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("GAV JavaFX");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }
    
    public static void buildGraph() {
        Node n0  = MyGraph.getNode(); n0 .setCenterX(100); n0 .setCenterY(280);
        Node n1  = MyGraph.getNode(); n1 .setCenterX(100); n1 .setCenterY(100);
        Node n2  = MyGraph.getNode(); n2 .setCenterX(50 ); n2 .setCenterY(170);
        Node n3  = MyGraph.getNode(); n3 .setCenterX(170); n3 .setCenterY(250);
        Node n4  = MyGraph.getNode(); n4 .setCenterX(270); n4 .setCenterY(210);
        Node n5  = MyGraph.getNode(); n5 .setCenterX(300); n5 .setCenterY(100);
        Node n6  = MyGraph.getNode(); n6 .setCenterX(400); n6 .setCenterY(170);
        Node n7  = MyGraph.getNode(); n7 .setCenterX(550); n7 .setCenterY(250);
        Node n8  = MyGraph.getNode(); n8 .setCenterX(240); n8 .setCenterY(370);
        Node n9  = MyGraph.getNode(); n9 .setCenterX(100); n9 .setCenterY(500);
        Node n10 = MyGraph.getNode(); n10.setCenterX(101); n10.setCenterY(670);
        Node n11 = MyGraph.getNode(); n11.setCenterX(400); n11.setCenterY(600);
        Node n12 = MyGraph.getNode(); n12.setCenterX(400); n12.setCenterY(480);
        Node n13 = MyGraph.getNode(); n13.setCenterX(370); n13.setCenterY(380);
        
        Node n14 = MyGraph.getNode(); n14.setCenterX(600); n14.setCenterY(600);
        
        MyGraph.getInstance().addNode(n0);
        MyGraph.getInstance().addNode(n1);
        MyGraph.getInstance().addNode(n2);
        MyGraph.getInstance().addNode(n3);
        MyGraph.getInstance().addNode(n4);
        MyGraph.getInstance().addNode(n5);
        MyGraph.getInstance().addNode(n6);
        MyGraph.getInstance().addNode(n7);
        MyGraph.getInstance().addNode(n8);
        MyGraph.getInstance().addNode(n9);
        MyGraph.getInstance().addNode(n10);
        MyGraph.getInstance().addNode(n11);
        MyGraph.getInstance().addNode(n12);
        MyGraph.getInstance().addNode(n13);
        MyGraph.getInstance().addNode(n14);
        
        MyGraph.getInstance().addEdge(n0, n1);
        MyGraph.getInstance().addEdge(n1, n2);
        MyGraph.getInstance().addEdge(n1, n3);
        
        MyGraph.getInstance().addEdge(n3, n4);
        MyGraph.getInstance().addEdge(n4, n5);
        MyGraph.getInstance().addEdge(n5, n6);
        MyGraph.getInstance().addEdge(n6, n7);
        MyGraph.getInstance().addEdge(n7, n8);
        MyGraph.getInstance().addEdge(n8, n9);
        MyGraph.getInstance().addEdge(n9, n10);
        MyGraph.getInstance().addEdge(n10, n11);
        MyGraph.getInstance().addEdge(n11, n12);
        MyGraph.getInstance().addEdge(n12, n13);
        
        MyGraph.getInstance().addEdge(n12, n7);
        MyGraph.getInstance().addEdge(n11, n8);
        MyGraph.getInstance().addEdge(n3, n7);
        MyGraph.getInstance().addEdge(n8, n13);
    }
}