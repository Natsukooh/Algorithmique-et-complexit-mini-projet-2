package fr.polytech.graph_builder;

import fr.polytech.exception.EdgeAlreadyExistsException;
import fr.polytech.exception.NodeNameAlreadyTakenException;
import fr.polytech.graph.Color;
import fr.polytech.graph.Edge;
import fr.polytech.graph.Graph;
import fr.polytech.graph.Node;

import java.util.Random;

/*
A graph builder to randomly build a specific amount of nodes aligned, each node having a certain chance of being red, with 1 edge between each node, having a certain chance to point left and a certain chance to be red.
 */
public class RandomLinearGraphBuilder implements GraphBuilder
{
    // The amount of nodes in the graph
    private final int nodesNumber;

    // The probability for each node to be red
    private final float redNodeChance;

    // The probability for each edge to be red
    private final float redEdgeChance;

    // The probability for each edge to point left
    private final float vertexPointingToLeftChance;

    private final Random randomNumberGenerator = new Random();

    /*
    We create a new builder with the given probabilities
     */
    public RandomLinearGraphBuilder(int nodesNumber, float redNodeChance, float redEdgeChance, float vertexPointingToLeftChance)
    {
        this.nodesNumber = nodesNumber;
        this.redNodeChance = redNodeChance;
        this.redEdgeChance = redEdgeChance;
        this.vertexPointingToLeftChance = vertexPointingToLeftChance;
    }

    /*
    This method builds a graph with the probabilities it has been given in the constructor, and returns it.
     */
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
                graph.getNodeByName(nodeName)
                        .get()
                        .addOutgoingEdge(new Edge(
                                edgeColor,
                                graph.getNodeByName(nodeName).get(),
                                graph.getNodeByName(arrivalNodeName).get()));
            }
            catch (EdgeAlreadyExistsException ignored) { /* this should never happen */ }
        }

        return graph;
    }
}
