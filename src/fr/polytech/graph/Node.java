package fr.polytech.graph;

import fr.polytech.exception.EdgeAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Node extends Colored
{
    private final String name;
    private List<Edge> edges;

    public Node(Color color, String name)
    {
        super(color);
        this.name = name;
        this.edges = new ArrayList<>();
    }

    public Node(Color color, String name, List<Edge> edges)
    {
        this(color, name);
        this.edges = edges;
    }

    public void addEdge(Edge edge) throws EdgeAlreadyExistsException
    {
        if(getNextEdgeByName(edge.getEnd().getName()).isPresent())
        {
            throw new EdgeAlreadyExistsException();
        }

        edges.add(edge);
    }

    public Optional<Node> getNextEdgeByName(String name)
    {
        return edges
                .stream()
                .map(Edge::getEnd)
                .filter(end -> end.getName().equals(name))
                .findAny();
    }

    public String getName()
    {
        return name;
    }

    public List<Edge> getEdges()
    {
        return edges;
    }

    @Override
    public String toString()
    {
        return "Node{" +
                "color=" + color +
                ", name='" + name + '\'' +
                ", edges=" + edges +
                '}';
    }
}
