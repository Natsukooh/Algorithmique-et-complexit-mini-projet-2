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
        GraphBuilder gb = new RandomLinearGraphBuilder(10, 0.5f, 0.5f, 0.5f);
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

        System.out.println(SIMULATIONS + " simulations done.\nIn " + equalities + " simulations, the score based algorithm has found the max red sequence.");
    }
}
