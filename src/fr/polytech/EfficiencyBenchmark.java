package fr.polytech;

import fr.polytech.graph.Graph;
import fr.polytech.graph_builder.GraphBuilder;
import fr.polytech.graph_builder.RandomLinearGraphBuilder;
import fr.polytech.solver.RedBlueMaximizationScoreBasedSolver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EfficiencyBenchmark
{
    static List<String> buffer = new ArrayList<>();
    final static String FILE_NAME = "results\\algoV1.csv";
    final static int NODES = 100;

    public static void main(String[] args)
    {
        int simulations = 0;

        if(args.length > 0)
        {
            simulations = Integer.parseInt(args[0]);
        }

        final int SIMULATIONS = (simulations == 0 ? 100 : simulations);

        for(float redNodeProbability = 0f; redNodeProbability < 1.05f; redNodeProbability += 0.1f)
        {
            for(float redEdgeProbability = 0f; redEdgeProbability < 1.05f; redEdgeProbability += 0.1f)
            {
                GraphBuilder builder = new RandomLinearGraphBuilder(NODES, redNodeProbability, redEdgeProbability, 0.5f);

                for(int i = 0; i < SIMULATIONS; i++)
                {
                    Graph graph = builder.buildGraph();
                    int result = graph.solve(new RedBlueMaximizationScoreBasedSolver());
                    addToBuffer(normalize(redNodeProbability) + "|" + normalize(redEdgeProbability) + "|" + result);
                }
            }
        }

        flushBuffer();
    }

    private static void addToBuffer(String line)
    {
        buffer.add(line);

        if(buffer.size() >= 1000)
        {
            flushBuffer();
        }
    }

    private static void flushBuffer()
    {
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME, true);

            buffer.forEach(line ->
            {
                try
                {
                    fileOutputStream.write((line + "\n").getBytes());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });

            fileOutputStream.close();
            buffer = new ArrayList<>();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static String normalize(float f)
    {
        return String.valueOf(f).substring(0, 3);
    }
}
