package fr.polytech;

import fr.polytech.graph.Graph;
import fr.polytech.graph_builder.GraphBuilder;
import fr.polytech.graph_builder.RandomLinearGraphBuilder;
import fr.polytech.solver.RedBlueMaximizationNaiveSolver;
import fr.polytech.solver.RedBlueMaximizationScoreBasedSolver;

public class ScoreBasedSolverTest
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
        final int SIMULATIONS = 1000;
        int equalities = 0;
        for(int i = 0; i < SIMULATIONS; i++)
        {
            Graph graph = gb.buildGraph();
            int scoreBasedResult = graph.solve(new RedBlueMaximizationScoreBasedSolver());
            int naiveResult = graph.solve(new RedBlueMaximizationNaiveSolver());

            if(scoreBasedResult == naiveResult)
            {
                equalities++;
            }
        }

        /*
        Finally, we print out the results.
         */
        System.out.println(SIMULATIONS + " simulations done.\nIn " + equalities + " simulations, the score based algorithm has found the max red sequence.");
    }
}
