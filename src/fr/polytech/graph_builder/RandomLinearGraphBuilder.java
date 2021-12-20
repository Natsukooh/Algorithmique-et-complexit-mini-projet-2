package fr.polytech.graph_builder;

import fr.polytech.exception.EdgeAlreadyExistsException;
import fr.polytech.exception.NodeNameAlreadyTakenException;
import fr.polytech.graph.Color;
import fr.polytech.graph.Edge;
import fr.polytech.graph.Graph;
import fr.polytech.graph.Node;

import java.util.Random;

public class RandomLinearGraphBuilder implements GraphBuilder
{
    private final int nodesNumber;
    private final float redNodeChance;
    private final float redEdgeChance;
    private final float vertexPointingToLeftChance;

    private final Random randomNumberGenerator = new Random();

    public RandomLinearGraphBuilder(int nodesNumber, float redNodeChance, float redEdgeChance, float vertexPointingToLeftChance)
    {
        this.nodesNumber = nodesNumber;
        this.redNodeChance = redNodeChance;
        this.redEdgeChance = redEdgeChance;
        this.vertexPointingToLeftChance = vertexPointingToLeftChance;
    }

    @Override
    public Graph buildGraph()
    {
        Graph graph = new Graph();

        for(int i = 0; i < nodesNumber; i++)
        {
            try
            {
                Color color = randomNumberGenerator.nextFloat() < redNodeChance ? Color.RED : Color.BLUE;
                String name = String.valueOf(i);
                graph.addNode(new Node(color, name));
            }
            catch (NodeNameAlreadyTakenException ignored) { /* this should never happen */ }
        }

        for(int i = 0; i < nodesNumber - 1; i++)
        {
            String nodeName = String.valueOf(randomNumberGenerator.nextFloat() < vertexPointingToLeftChance ? i + 1 : i);
            String arrivalNodeName = String.valueOf(nodeName.equals(String.valueOf(i)) ? i + 1 : i);
            Color edgeColor = randomNumberGenerator.nextFloat() < redEdgeChance ? Color.RED : Color.BLUE;

            try
            {
                //noinspection OptionalGetWithoutIsPresent
                graph.getNodeByName(nodeName).get().addEdge(new Edge(edgeColor, graph.getNodeByName(arrivalNodeName).get()));
            }
            catch (EdgeAlreadyExistsException ignored) { /* this should never happen */ }
        }

        return graph;
    }
}
