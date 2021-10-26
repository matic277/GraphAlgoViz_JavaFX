package com.example.gav_fx.core;

import javafx.scene.paint.Color;

public enum OutputType {
    // TODO include threadName here? maybe not...
    DEBUG(Color.rgb(220, 220, 220)),
    WARNING(Color.rgb(231, 160, 45)),
    ERROR(Color.rgb(220, 67, 67)),
    ALGO_CONTROLLER(Color.rgb(68, 215, 169)),
    ALGO_EXECUTOR(Color.rgb(40, 177, 160)),
    ;
    
    private final Color fontColor;
    
    OutputType(Color c) { fontColor = c; }
    
    public Color getColor() { return fontColor; }
}
