package com.example.gav_fx.panes.rightpane;

public enum TableDataType {
    NODES("Number of nodes"),
    EDGES("Number of edges"),
    ;
    private final String name;
    TableDataType(String n) { name = n; }
    public String getName() { return name; }
}
