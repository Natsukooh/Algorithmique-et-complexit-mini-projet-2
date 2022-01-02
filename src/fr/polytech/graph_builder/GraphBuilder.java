package fr.polytech.graph_builder;

import fr.polytech.graph.Graph;

/*
Interface for a graph builder.
There are multiple ways to build a graph. You can for example build one from a user's input, or build a random one.
Make a class that implements this interface to implement your own way to build a graph.
 */
public interface GraphBuilder
{

    /*
    Make this method build a graph object and return it.
     */
    Graph buildGraph();
}
