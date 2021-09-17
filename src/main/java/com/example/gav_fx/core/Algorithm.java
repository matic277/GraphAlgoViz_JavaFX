package com.example.gav_fx.core;

/**
 * An algorithm accepts the node in which it's being executed.
 * Returns new state of accepted node.
 */
@FunctionalInterface
public interface Algorithm {
    public State run(Vertex vertex); // this could probably just be a Function<Vertex, State>
}
