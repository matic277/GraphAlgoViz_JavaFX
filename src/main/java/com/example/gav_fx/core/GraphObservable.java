package com.example.gav_fx.core;

public interface GraphObservable {
    
    void addObserver(GraphChangeObserver observer);
    void removeObserver(GraphChangeObserver observer);
}
