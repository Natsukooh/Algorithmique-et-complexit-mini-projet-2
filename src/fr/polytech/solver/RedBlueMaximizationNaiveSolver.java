package fr.polytech.solver;

import fr.polytech.graph.Color;
import fr.polytech.graph.Graph;
import fr.polytech.graph.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
Naive solver.
Recursively looks for all the possibilities, and returns the longest red sequence possible.
Is quite fast when the amount of nodes is lower than 15, but after that it becomes extremely slow (the complexity is exponential).
 */
public class RedBlueMaximizationNaiveSolver implements RedBlueMaximizationSolver
{
    public int solve(Graph graph)
    {
        List<Integer> scores = new ArrayList<>();

        for(Node node : graph.getNodes())
        {
            if(node.getColor().equals(Color.RED))
            {
                //noinspection OptionalGetWithoutIsPresent
                Graph popped = graph.popRedNodeAndPropagateRed(node).get();
                scores.add(solve(popped, 1));
            }
        }

        Optional<Integer> optionalScore = scores.stream().max(Integer::compare);
        return optionalScore.orElse(0);
    }

    private static int solve(Graph graph, int depth)
    {
        if(graph.getNodes()
                .stream()
                .filter(node -> node.getColor().equals(Color.RED))
                .findAny()
                .isEmpty())
        {
            return depth;
        }

        List<Integer> scores = new ArrayList<>();
        int maxScore = 0;
        String maxNodeName = "";

        for(Node node : graph.getNodes())
        {
            if(node.getColor().equals(Color.RED))
            {
                //noinspection OptionalGetWithoutIsPresent
                Graph popped = graph.popRedNodeAndPropagateRed(node).get();
                int score = solve(popped, depth + 1);
                if (score > maxScore)
                {
                    maxScore = score;
                }
            }
        }

        return maxScore;
    }
}
