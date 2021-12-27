package fr.polytech.solver;

import fr.polytech.graph.Color;
import fr.polytech.graph.Graph;
import fr.polytech.graph.Node;

import java.util.Comparator;
import java.util.Optional;

public class RedBlueMaximizationScoreBasedSolver implements RedBlueMaximizationSolver
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
                    .max(Comparator.comparingInt(this::getScore));

            if (optionalNode.isPresent())
            {
                Node bestNode = optionalNode.get();
                //noinspection OptionalGetWithoutIsPresent
                graph = graph.popRedNodeAndPropagateRed(bestNode).get();
                deleted++;
            }
            else
            {
                return deleted;
            }
        }
    }

    private int getScore(Node node)
    {
        final int[] score = {0};

        node.getOutgoingEdges().forEach(edge ->
        {
            if(edge.getColor().equals(Color.BLUE) && edge.getEnd().getColor().equals(Color.RED))
            {
                score[0]--;
            }
            else if(edge.getColor().equals(Color.RED) && edge.getEnd().getColor().equals(Color.BLUE))
            {
                score[0]++;
            }
        });

        return score[0];
    }
}
