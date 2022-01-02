package fr.polytech;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
This class is used to make a JAR executable to build a .csv file of the table for the exercise 7.
Usage : java -jar BuildTable.jar <data file name> <output file name>
The data file name must be given. The output file name is not mandatory, by default it is ./data.csv
 */
public class BuildTable
{
    private static String DATA_FILE_NAME = "";
    private static String OUTPUT_FILE_NAME = "data.csv";

    public static void main(String[] args)
    {
        scanArgs(args);

        double[][] averages = new double[11][11];
        List<List<List<Integer>>> buffer = new ArrayList<>();

        for(int x = 0; x < 11; x++)
        {
            buffer.add(new ArrayList<>());
            for(int y = 0; y < 11; y++)
            {
                buffer.get(x).add(new ArrayList<>());
            }
        }

        BufferedReader dataFileReader = null;

        try
        {
            dataFileReader = new BufferedReader(new FileReader(DATA_FILE_NAME));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error : file " + OUTPUT_FILE_NAME + " not found ! Exiting.");
            System.exit(1);
        }

        dataFileReader.lines()
                .forEach(line ->
                {
                    String[] splittedLine = line.split("\\|");
                    double redNodeProbability = Double.parseDouble(splittedLine[0]);
                    double redEdgeProbability = Double.parseDouble(splittedLine[1]);
                    int deletedNodes = Integer.parseInt(splittedLine[2]);
                    int x = asCoord(redNodeProbability);
                    int y = asCoord(redEdgeProbability);
                    buffer.get(x).get(y).add(deletedNodes);
                });

        for(int x = 0; x < 11; x++)
        {
            for(int y = 0; y < 11; y++)
            {
                double average = buffer.get(x).get(y)
                        .stream()
                        .mapToDouble(a -> a)
                        .average()
                        .orElse(0);

                averages[x][y] = average;
            }
        }

        try
        {
            BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));
            outputFileWriter.write("p q|0|0.1|0.2|0.3|0.4|0.5|0.6|0.7|0.8|0.9|1\n");

            for(int x = 0; x < 11; x++)
            {
                outputFileWriter.write(asProbablilty(x) + "|");
                for (int y = 0; y < 11; y++)
                {
                    outputFileWriter.write(normalizeAverage(averages[x][y], 5) + (y == 10 ? "\n" : "|"));
                }
            }

            outputFileWriter.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur lors de l'ouverture du fichier " + OUTPUT_FILE_NAME + " !");
            e.printStackTrace();
        }
    }

    private static int asCoord(double f)
    {
        return (int) Math.round(10*f);
    }

    private static String asProbablilty(int x)
    {
        return (x == 0) ? "0" : String.valueOf(((double) x) / 10).substring(0, 3);
    }

    private static String normalizeAverage(double average, int length)
    {
        String str =  String.valueOf(average);

        if(str.length() <= length)
        {
            return str;
        }
        else
        {
            return str.substring(0, length);
        }
    }

    private static void scanArgs(String[] args)
    {
        if (args.length > 0)
        {
            DATA_FILE_NAME = args[0];

            if(args.length > 1)
            {
                OUTPUT_FILE_NAME = args[1];
            }
        }
    }
}
