package fr.polytech;

import fr.polytech.graph.Graph;
import fr.polytech.graph_builder.GraphBuilder;
import fr.polytech.graph_builder.RandomLinearGraphBuilder;
import fr.polytech.solver.RedBlueMaximizationBasicSolver;
import fr.polytech.solver.RedBlueMaximizationNaiveSolver;
import fr.polytech.solver.RedBlueMaximizationScoreBasedSolver;

public class SolversTests
{
    public static void main(String[] args)
    {
        /*
        The graph generator.
        each graph will have 10 nodes, each having a 50% chance of being red.
        the edges also have a 50% chance to be red, and another 50% chance to be pointing left.
        using the buildGraph() method on this object will get you a new Graph with those parameters.
        */
        GraphBuilder gb = new RandomLinearGraphBuilder(10, 0.5f, 0.5f, 0.5f);

        /*
        Here we are gonna generate a large amount of graphs, and test the score based solver, to compare its results to the naive solver, which gives the best possible answer but is slow on big graphs.
         */
        final int SIMULATIONS = 10000;
        int equalitiesAlgo1 = 0;
        int equalitiesAlgo2 = 0;

        for(int i = 0; i < SIMULATIONS; i++)
        {
            Graph graph = gb.buildGraph();
            int algoV1Result = graph.solve(new RedBlueMaximizationScoreBasedSolver());
            int algoV2Result = graph.solve(new RedBlueMaximizationBasicSolver());
            int naiveResult = graph.solve(new RedBlueMaximizationNaiveSolver());

            if(algoV1Result == naiveResult)
            {
                equalitiesAlgo1++;
            }

            if(algoV2Result == naiveResult)
            {
                equalitiesAlgo2++;
            }
        }

        /*
        Finally, we print out the results.
         */
        System.out.println(SIMULATIONS + " simulations done.\nIn " + equalitiesAlgo1 + " simulations, the V1 algorithm has found the max red sequence.\nIn " + equalitiesAlgo2 + " simulations, the V2 algorithm has found the max red sequence.");
    }
}
