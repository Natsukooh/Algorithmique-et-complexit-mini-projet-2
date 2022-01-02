package fr.polytech.solver;

import fr.polytech.graph.Color;
import fr.polytech.graph.Graph;
import fr.polytech.graph.Node;

import java.util.Optional;

/*
The second algorithm described in the question 4.
 */
public class RedBlueMaximizationBasicSolver implements RedBlueMaximizationSolver
{
    @Override
    public int solve(Graph graph)
    {
        int deleted = 0;

        while(true)
        {
            // Here we try to find a red node that has a red outgoing edge pointing to a blue node.
            Optional<Node> optionalNode = graph.getNodes()
                    .stream()
                    .filter(node -> node.getColor().equals(Color.RED))
                    .filter(node ->
                    {
                        return node.getOutgoingEdges()
                                .stream()
                                .anyMatch(edge -> edge.getColor().equals(Color.RED) && edge.getEnd().getColor().equals(Color.BLUE));
                    })
                    .findAny();

            // If we found one, we can add it to the sequence and go on.
            if(optionalNode.isPresent())
            {
                //noinspection OptionalGetWithoutIsPresent
                graph = graph.popRedNodeAndPropagateRed(optionalNode.get()).get();
                deleted++;
            }
            // Else, we look for another red node.
            else
            {
                // This time we look for a red node that doesn't have any blue outgoing edge pointing to a red node.
                optionalNode = graph.getNodes()
                        .stream()
                        .filter(node -> node.getColor().equals(Color.RED))
                        .filter(node ->
                        {
                            return node.getOutgoingEdges()
                                    .stream()
                                    .noneMatch(edge -> edge.getColor().equals(Color.BLUE) && edge.getEnd().getColor().equals(Color.RED));
                        })
                        .findAny();

                // If we found one, we can add it to the sequence and go on.
                if(optionalNode.isPresent())
                {
                    //noinspection OptionalGetWithoutIsPresent
                    graph = graph.popRedNodeAndPropagateRed(optionalNode.get()).get();
                    deleted++;
                }
                // Else, we look for another red node.
                else
                {
                    // Here we are looking for any red node.
                    optionalNode = graph.getNodes()
                            .stream()
                            .filter(node -> node.getColor().equals(Color.RED))
                            .findAny();

                    // If there's still some, we can add it to the sequence and go on.
                    if(optionalNode.isPresent())
                    {
                        //noinspection OptionalGetWithoutIsPresent
                        graph = graph.popRedNodeAndPropagateRed(optionalNode.get()).get();
                        deleted++;
                    }
                    // Else there's no more red nodes, we can stop the function and return the sequence's length.
                    else
                    {
                        return deleted;
                    }
                }
            }
        }
    }
}
