package fr.polytech.solver;

import fr.polytech.graph.Color;
import fr.polytech.graph.Graph;
import fr.polytech.graph.Node;

import java.util.Optional;

public class RedBlueMaximizationBasicSolver implements RedBlueMaximizationSolver
{
    @Override
    public int solve(Graph graph)
    {
        int deleted = 0;

        while(true)
        {
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

            if(optionalNode.isPresent())
            {
                //noinspection OptionalGetWithoutIsPresent
                graph = graph.popRedNodeAndPropagateRed(optionalNode.get()).get();
                deleted++;
            }
            else
            {
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

                if(optionalNode.isPresent())
                {
                    //noinspection OptionalGetWithoutIsPresent
                    graph = graph.popRedNodeAndPropagateRed(optionalNode.get()).get();
                    deleted++;
                }
                else
                {
                    optionalNode = graph.getNodes()
                            .stream()
                            .filter(node -> node.getColor().equals(Color.RED))
                            .findAny();

                    if(optionalNode.isPresent())
                    {
                        //noinspection OptionalGetWithoutIsPresent
                        graph = graph.popRedNodeAndPropagateRed(optionalNode.get()).get();
                        deleted++;
                    }
                    else
                    {
                        return deleted;
                    }
                }
            }
        }
    }
}
