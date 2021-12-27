package fr.polytech.graph_builder;

import fr.polytech.exception.EdgeAlreadyExistsException;
import fr.polytech.graph.Color;
import fr.polytech.graph.Edge;
import fr.polytech.graph.Graph;
import fr.polytech.graph.Node;
import fr.polytech.exception.ColorFormatException;
import fr.polytech.exception.NodeNameAlreadyTakenException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class InputGraphBuilder implements GraphBuilder
{
    private InputStream inputStream;

    public InputGraphBuilder()
    {
        inputStream = System.in;
    }

    public InputGraphBuilder(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    public InputGraphBuilder(File file)
    {
        try
        {
            inputStream = new FileInputStream(file);
        }
        catch(FileNotFoundException e)
        {
            System.err.println("File error : cannot open file " + file.getAbsolutePath() + ".");
        }
    }

    @Override
    public Graph buildGraph()
    {
        if(inputStream == null)
        {
            System.err.println("File error : input stream not initialized. Reading to console.");
        }

        Graph graph = new Graph();
        List<Node> nodes;
        Scanner scanner = new Scanner(inputStream == null ? System.in : inputStream);
        int lineNumber = 1;
        String line = scanner.nextLine();

        // First, scanning the nodes
        while(!line.equals(""))
        {
            String[] splittedLine = line.split(" ");

            if(splittedLine.length < 2)
            {
                System.err.println("Format error at line " + lineNumber + " : the line doesn't contain enough elements (" + splittedLine.length + " provided, 2 required).");
            }
            else if(splittedLine.length > 2)
            {
                System.out.println("Warning : format error at line " + lineNumber + " : the line contains too much elements (" + splittedLine.length + " provided, 2 required). Elements after " + splittedLine[1] + " will be ignored.");
            }

            String name = line.split(" ")[0];
            String colorS = line.split(" ")[1];
            Color color;

            try
            {
                color = Color.toColor(colorS);
                Node node = new Node(color, name);
                graph.addNode(node);
            }
            catch(ColorFormatException e)
            {
                System.err.println("Format error at line " + lineNumber + " : cannot parse " + colorS + " as a color. Line ignored.");
            }
            catch(NodeNameAlreadyTakenException e)
            {
                System.err.println("Name error at line " + lineNumber + " : cannot create node with name " + name + " because there's already a node with that name in the graph.");
            }

            lineNumber++;
            line = scanner.nextLine();
        }

        // Then, we get the edges
        while(scanner.hasNext())
        {
            line = scanner.nextLine();
            String[] splittedLine = line.split(" ");

            if (splittedLine.length < 3)
            {
                System.err.println("Format error at line " + lineNumber + " : the line doesn't contain enough elements (" + splittedLine.length + " provided, 3 required).");
            }
            else if(splittedLine.length > 3)
            {
                System.out.println("Warning : format error at line " + lineNumber + " : the line contains too much elements (" + splittedLine.length + " provided, 3 required). Elements after " + splittedLine[2] + " will be ignored.");
            }

            String startName = splittedLine[0];
            String endName = splittedLine[1];
            String colorS = splittedLine[2];

            Optional<Node> startNodeSearch = graph.getNodeByName(startName);
            Optional<Node> endNodeSearch = graph.getNodeByName(endName);
            Node startNode;
            Node endNode;

            try
            {
                //noinspection OptionalGetWithoutIsPresent
                startNode = startNodeSearch.get();
                //noinspection OptionalGetWithoutIsPresent
                endNode = endNodeSearch.get();
                Color color = Color.toColor(colorS);
                Edge edge = new Edge(color, startNode, endNode);
                startNode.addOutgoingEdge(edge);
            }
            catch (NoSuchElementException e)
            {
                System.err.println("Name error at line " + lineNumber + " : no node named " + startName + " in the graph.");
            }
            catch(ColorFormatException e)
            {
                System.err.println("Format error at line " + lineNumber + " : cannot parse " + colorS + " as a color. Line ignored.");
            }
            catch (EdgeAlreadyExistsException e)
            {
                System.err.println("Name error at line " + lineNumber + " : an edge starting from node " + startName + " and going to node " + endName + " already exists.");
            }

            lineNumber++;
        }

        return graph;
    }
}
