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
        if(this.getNodeByName(node.getName()).isPresent())
        {
            throw new NodeNameAlreadyTakenException();
        }

        nodes.add(node);
    }

    public Optional<Node> getNodeByName(String name)
    {
        return nodes.stream().filter(node -> node.getName().equals(name)).findAny();
    }
}
