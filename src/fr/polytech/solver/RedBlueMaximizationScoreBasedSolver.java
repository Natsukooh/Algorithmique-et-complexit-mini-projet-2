package fr.polytech.solver;

import fr.polytech.graph.Color;
import fr.polytech.graph.Graph;
import fr.polytech.graph.Node;

import java.util.Comparator;
import java.util.Optional;

/*
The first algorithm described in the question 4.
 */
public class RedBlueMaximizationScoreBasedSolver implements RedBlueMaximizationSolver
{

    @Override
    public int solve(Graph graph)
    {
        int deleted = 0;

        /*
        Here, we take all the red nodes of the graph and attempt to get the one that has the best score.
        If it exists, we add it to the red sequence, and we go on. Else, that's because there's no more red nodes in the graph, and we then return the length of the sequence.
         */
        while(true)
        {
            Optional<Node> optionalNode = graph.getNodes()
                    .stream()
                    .filter(node -> node.getColor().equals(Color.RED))
                    .max(Comparator.comparingInt(this::getScore));

            // If it is present, there's still at least one red node in the graph.
            if (optionalNode.isPresent())
            {
                Node bestNode = optionalNode.get();
                //noinspection OptionalGetWithoutIsPresent
                graph = graph.popRedNodeAndPropagateRed(bestNode).get();
                deleted++;
            }
            // Else, there's no more red nodes in the graph, and it is over.
            else
            {
                return deleted;
            }
        }
    }

    /*
    To get a node's score, we take each of its outgoing edges.
    If there's a blue one pointing to a red node, then we subtract 1 to the score.
    If there's a red one pointing to a blue node, then we add 1 to the score.
    In other cases we do nothing.
     */
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
