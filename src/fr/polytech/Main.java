package fr.polytech;

import fr.polytech.graph.Graph;
import fr.polytech.graph_builder.GraphBuilder;
import fr.polytech.graph_builder.InputGraphBuilder;
import fr.polytech.graph_builder.RandomLinearGraphBuilder;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        /*
        GraphBuilder builder = new InputGraphBuilder(new File("D:\\windows\\Users\\romai\\Documents\\SI4\\Algorithmique et complexité\\projet_algo\\inputs\\projet_example"));
        Graph graph = builder.buildGraph();
        System.out.println(graph);
        */

        GraphBuilder builder2 = new RandomLinearGraphBuilder(10, 0.5f, 0.5f, 0.5f);
        Graph graph2 = builder2.buildGraph();
        System.out.println(graph2);
    }
}
