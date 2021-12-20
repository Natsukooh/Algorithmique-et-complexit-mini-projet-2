package fr.polytech;

import fr.polytech.graph.Graph;
import fr.polytech.graph_builder.GraphBuilder;
import fr.polytech.graph_builder.InputGraphBuilder;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        GraphBuilder builder = new InputGraphBuilder(new File("D:\\windows\\Users\\romai\\Documents\\SI4\\Algorithmique et complexité\\projet_algo\\inputs\\projet_example"));
        Graph graph = builder.buildGraph();
        System.out.println(graph);
    }
}
