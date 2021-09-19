package com.example.gav_fx.core;


import com.example.gav_fx.graphbuilder.*;
import com.example.gav_fx.panes.optionpanes.*;

public enum ImportType {
    
    // testing purposes
    STATIC_TEST(0, "STATIC_TEST",              new StaticTestGraphOptionPanel(), new StaticTestGraphBuilder()),
    RANDOM(     1, "Random graph",             new RandomGraphOptionPanel(),     new RandomGraphBuilder()),
    FILE(       2, "Load from file",           new FileGraphOptionPanel(),       new FileGraphBuilder()),
    USER(       3, "Create your own",          new UserGraphOptionPanel(),       new EmptyGraphBuilder()),
    CLIQUE(     4, "Fully connected (clique)", new CliqueGraphOptionPanel(),     new CliqueGraphBuilder());
    
    private final int id;
    private final String description;
    private final OptionPane pane;
    private final GraphBuilder builder;
    
    ImportType(int id, String desc, OptionPane pane, GraphBuilder builder) {
        this.id = id;
        this.description = desc;
        this.pane = pane;
        this.builder = builder;
    }
    
    public static ImportType getByDescription(String desc) {
        for (ImportType value : ImportType.values()) {
            if (value.description.equals(desc)) return value;
        }
        throw new RuntimeException("Unknown graph type " + desc + ".");
    }
    
    public OptionPane getOptionPane() { return this.pane; }
    public GraphBuilder getGraphBuilder() { return this.builder; }
    
    @Override
    public String toString() {
        return description;
    }
}
