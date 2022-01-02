package fr.polytech;

import fr.polytech.graph.Graph;
import fr.polytech.graph_builder.GraphBuilder;
import fr.polytech.graph_builder.RandomLinearGraphBuilder;
import fr.polytech.solver.RedBlueMaximizationBasicSolver;
import fr.polytech.solver.RedBlueMaximizationScoreBasedSolver;
import fr.polytech.solver.RedBlueMaximizationSolver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
This class is used to make a runnable JAR which will repetitively generate some graphs, solve the with both our algorithms and save the result to a file.
The results file will then be used by the BuildTable class to build a .csv file which can then be opened with excel to visualize the results.

This program may take some arguments : as a jar, you can run it with the following command :
java -jar EfficiencyBenchmark.jar <first algorithm results file> <second algorithm results file> <stop file path> <numbers of simulations for each case of the table>
where <first algorithm results file> is a path to a file where the results of the first algorithm will be written,
<second algorithm results file> is a path to a file where the results of the second algorithm will be written,
<stop file path> is a path to a file that will stop the program if it exists (the program basically runs in an infinite loop, and exists when this file exists, so create this file when you want to stop the program),
and <numbers of simulations for each case of the table> is exactly what its name says.
Since the table size is 11*11, there are 121 cells in total, so the numbers of graphs generated and solved will be 121 * <numbers of simulations for each case of the table>.
Once this amount of simulations done, the program checks the existence of the stop file ; if found, the program stops. Else, it keeps going.

Since the arguments are not mandatory, the default parameters are :
<first algorithm results file> : ./algoV1.csv
<second algorithm results file> : ./algoV2.csv
<stop file path> : ./stop
<numbers of simulations for each case of the table> : 100

Example command to run the program :
java -jar EfficiencyBenchmark.jar /home/username/miniprojet_2_algo/algoV1.csv /home/username/miniprojet_2_algo/algoV2.csv /home/username/miniprojet_2_algo/stop 10
 */

public class EfficiencyBenchmark
{
    private static List<String> v1_buffer = new ArrayList<>();
    private static List<String> v2_buffer = new ArrayList<>();
    private final static RedBlueMaximizationSolver v1_solver = new RedBlueMaximizationScoreBasedSolver();
    private final static RedBlueMaximizationSolver v2_solver = new RedBlueMaximizationBasicSolver();
    private final static int NODES = 100;

    private static String V1_OUTPUT_FILE_NAME = "algoV1.csv";
    private static String V2_OUTPUT_FILE_NAME = "algoV2.csv";
    private static String STOP_FILE_NAME = "stop";
    private static int SIMULATIONS = 100;

    public static void main(String[] args)
    {
        scanArgs(args);

        while(true)
        {
            File mustBeStopped = new File(STOP_FILE_NAME);
            if(mustBeStopped.exists() && !mustBeStopped.isDirectory())
            {
                System.out.println("File " + STOP_FILE_NAME + " found. Exiting.");
                break;
            }
            else
            {
                System.out.println("Beginning simulations. If you wanna end the program at some point, create the following file : "
                        + STOP_FILE_NAME
                        + ". When created, the program will end itself after a few seconds (depending on the amount of simulations in each cycle).");
            }

            for (float redNodeProbability = 0f; redNodeProbability < 1.05f; redNodeProbability += 0.1f)
            {
                for (float redEdgeProbability = 0f; redEdgeProbability < 1.05f; redEdgeProbability += 0.1f)
                {
                    GraphBuilder builder = new RandomLinearGraphBuilder(NODES, redNodeProbability, redEdgeProbability, 0.5f);

                    for (int i = 0; i < SIMULATIONS; i++)
                    {
                        Graph graph = builder.buildGraph();
                        int v1_result = graph.solve(v1_solver);
                        int v2_result = graph.solve(v2_solver);
                        addToBuffer(
                                normalize(redNodeProbability) + "|" + normalize(redEdgeProbability) + "|" + v1_result,
                                normalize(redNodeProbability) + "|" + normalize(redEdgeProbability) + "|" + v2_result);
                    }
                }
            }

            flushBuffers();
        }

        flushBuffers();
    }

    private static void addToBuffer(String v1_line, String v2_line)
    {
        v1_buffer.add(v1_line);
        v2_buffer.add(v2_line);

        if(v1_buffer.size() >= 1000 || v2_buffer.size() >= 1000)
        {
            flushBuffers();
        }
    }

    private static void flushBuffers()
    {
        try
        {
            FileOutputStream v1_fileOutputStream = new FileOutputStream(V1_OUTPUT_FILE_NAME, true);
            FileOutputStream v2_fileOutputStream = new FileOutputStream(V2_OUTPUT_FILE_NAME, true);

            v1_buffer.forEach(line ->
            {
                try
                {
                    v1_fileOutputStream.write((line + "\n").getBytes());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });

            v2_buffer.forEach(line ->
            {
                try
                {
                    v2_fileOutputStream.write((line + "\n").getBytes());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });

            v1_fileOutputStream.close();
            v1_buffer = new ArrayList<>();

            v2_fileOutputStream.close();
            v2_buffer = new ArrayList<>();
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

    private static void scanArgs(String[] args)
    {
        if(args.length > 0)
        {
            V1_OUTPUT_FILE_NAME = args[0];

            if(args.length > 1)
            {
                V2_OUTPUT_FILE_NAME = args[1];

                if(args.length > 2)
                {
                    STOP_FILE_NAME = args[2];

                    if(args.length > 3)
                    {
                        SIMULATIONS = Integer.parseInt(args[3]);
                    }
                }
            }
        }
    }
}
