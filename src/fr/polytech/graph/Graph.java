package fr.polytech.graph;

import fr.polytech.exception.NodeNameAlreadyTakenException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Graph
{
    private final List<Node> nodes;

    public Graph()
    {
        nodes = new ArrayList<>();
    }

    public Graph(List<Node> nodes)
    {
        this.nodes = nodes;
    }

    public void addNode(Node node) throws NodeNameAlreadyTakenException
    {
        if(this.getNodeByName(node.getName())
                .isPresent())
        {
            throw new NodeNameAlreadyTakenException();
        }

        nodes.add(node);
    }

    public Optional<Node> getNodeByName(String name)
    {
        return nodes.stream()
                .filter(node -> node.getName().equals(name))
                .findAny();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (Node node : nodes)
        {
            sb.append("Name: ").append(node.getName()).append(", Color: ").append(node.getColor()).append("\n");

            if(node.getEdges().size() == 0)
            {
                sb.append("No outgoing edge.\n");
            }
            else
            {
                for(Edge edge : node.getEdges())
                {
                    sb.append(node.getName()).append(" --").append(edge.getColor() == Color.RED ? "R" : "B").append("--> ").append(edge.getEnd().getName()).append("\n");
                }
            }

            sb.append("==============================\n");
        }

        return sb.toString();
    }
}
