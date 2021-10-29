package com.example.gav_fx.components.rightpane;

public class TableDataRow {
    TableDataType type;
    int value;
    public TableDataRow(TableDataType t) { type = t; value = 0; }
    
    public String getType() { return type.getName(); }
    public String getValue() { return value+""; }
}
