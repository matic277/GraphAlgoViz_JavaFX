package com.example.gav_fx.core;

public interface StateObservable {
	
	void addObserver(StateObserver obsever);
	void removeObserver(StateObserver observer);
	
}
