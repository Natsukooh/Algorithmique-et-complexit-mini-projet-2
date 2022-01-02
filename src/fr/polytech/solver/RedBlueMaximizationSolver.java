package fr.polytech.solver;

import fr.polytech.graph.Graph;

/*
Interface for a solver.
The method takes a graph in parameter and returns the size of the red sequence it found.
 */
public interface RedBlueMaximizationSolver
{
    int solve(Graph graph);
}
